package dev.syoritohatsuki.yetanotherairdrop.entity.projectile

import dev.syoritohatsuki.yetanotherairdrop.DatapackLoader
import dev.syoritohatsuki.yetanotherairdrop.YetAnotherAirDrop.logger
import dev.syoritohatsuki.yetanotherairdrop.dto.Drop
import dev.syoritohatsuki.yetanotherairdrop.entity.EntityTypeRegistry
import dev.syoritohatsuki.yetanotherairdrop.server.world.ModdedChunkTicketType
import dev.syoritohatsuki.yetanotherairdrop.world.AirDropManager
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.entity.BarrelBlockEntity
import net.minecraft.component.DataComponentTypes
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.MovementType
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.data.DataTracker
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.listener.ClientPlayPacketListener
import net.minecraft.network.packet.Packet
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket
import net.minecraft.registry.Registries
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.server.network.EntityTrackerEntry
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkSectionPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import net.minecraft.world.explosion.Explosion

class AirDropEntity(type: EntityType<AirDropEntity>, world: World) : Entity(type, world) {

    private var drop: Drop? = null
    private var tries: Byte = 0
    private var chunkTicketExpiryTicks: Long = 0

    companion object {
        val BLOCK: Block = Blocks.BARREL
    }

    init {
        this.setPosition(x, y, z)
        this.velocity = Vec3d.ZERO
        this.prevX = x
        this.prevY = y
        this.prevZ = z
    }

    constructor(world: World, x: Double, y: Double, z: Double) : this(EntityTypeRegistry.AIR_DROP, world) {
        setPosition(x, y, z)
        yaw = 0F
        setVelocity(
            (random.nextDouble() * 0.2f - 0.1f) * 2.0,
            random.nextDouble() * 0.2 * 2.0,
            (random.nextDouble() * 0.2f - 0.1f) * 2.0
        )
    }

    override fun isImmuneToExplosion(explosion: Explosion): Boolean = true

    override fun isFireImmune(): Boolean = true

    override fun damage(world: ServerWorld?, source: DamageSource?, amount: Float): Boolean {
        if (!this.isAlwaysInvulnerableTo(source)) this.scheduleVelocityUpdate()
        return false
    }

    private fun onGrounded(state: BlockState) {

        if (world.isClient) return

        (world as? ServerWorld)?.let { serverWorld ->

            drop = drop ?: DatapackLoader.getRandomDrop(serverWorld.registryKey.value) ?: run {
                discard()
                logger.warn("No drop found for ${serverWorld.registryKey.value} dimension")
                return
            }

            if (blockY == serverWorld.bottomY) {
                if (!drop!!.safePlatform) {
                    discard()
                    return
                }

                buildSafePlatform()
                trySetBarrel(state, blockPos.up())
                return
            }

            if (isOnGround) trySetBarrel(state, blockPos)
        }
    }

    private fun buildSafePlatform() {
        for (offsetX in -1..1) {
            for (offsetZ in -1..1) {
                world.setBlockState(
                    BlockPos(blockPos.x + offsetX, blockPos.y, blockPos.z + offsetZ),
                    Blocks.GLASS.defaultState,
                    Block.NOTIFY_ALL_AND_REDRAW
                )
            }
        }
    }

    override fun initDataTracker(builder: DataTracker.Builder?) {

    }

    override fun tick() {
        super.tick()

        if (world is ServerWorld && isAlive) {

            val blockPos = BlockPos.ofFloored(this.pos)
            if (--this.chunkTicketExpiryTicks <= 0L || ChunkSectionPos.getSectionCoordFloored(this.pos.x) != ChunkSectionPos.getSectionCoord(
                    blockPos.x
                ) || ChunkSectionPos.getSectionCoordFloored(this.pos.z) != ChunkSectionPos.getSectionCoord(blockPos.z)
            ) {
                this.chunkTicketExpiryTicks = AirDropManager.handleBarrelFly(this)
            }

            val blockState = world.getBlockState(blockPos)
            if (!blockState.isOf(Blocks.MOVING_PISTON)) {
                onGrounded(blockState)
            }
        }

        applyGravity()
        move(MovementType.SELF, velocity)

        velocity = velocity.multiply(0.98)
    }

    private fun trySetBarrel(state: BlockState, blockPos: BlockPos): Boolean {
        if (tries >= 3) {
            logger.error("Almost impossible, but can't set barrel on ground after 3 try :(")
            discard()
            return false
        }

        if (!BLOCK.defaultState.canPlaceAt(world, blockPos)) {
            tries++
            return trySetBarrel(state, blockPos.up(1))
        }

        if (world.setBlockState(blockPos, BLOCK.defaultState, Block.NOTIFY_ALL_AND_REDRAW)) {

            val randomTable = DatapackLoader.getRandomLootTable(drop!!)

            val blockEntity = world.getBlockEntity(blockPos) as BarrelBlockEntity

            server?.reloadableRegistries?.getIds(RegistryKeys.LOOT_TABLE)?.firstOrNull {
                it.toString() == randomTable
            }?.let {
                blockEntity.lootTable = RegistryKey.of(RegistryKeys.LOOT_TABLE, it)
            } ?: blockEntity.setStack(13, ItemStack(Items.PAPER).apply {
                set(DataComponentTypes.CUSTOM_NAME, Text.literal("ERROR! Loot table $randomTable not exist").styled {
                    it.withBold(true).withColor(Formatting.RED)
                })
            })

            (world as ServerWorld).run {
                chunkManager.chunkLoadingManager.sendToOtherNearbyPlayers(
                    this@AirDropEntity, BlockUpdateS2CPacket(blockPos, world.getBlockState(blockPos))
                )
                server.playerManager?.playerList?.filter {
                    it.world.registryKey.value == world.registryKey.value
                }?.forEach { player ->
                    drop?.sound?.let {
                        if (it.isBlank()) return@let
                        try {
                            logger.warn("PLAY: $it")
                            world.playSound(
                                this@AirDropEntity,
                                player.blockPos,
                                Registries.SOUND_EVENT.get(Identifier.of(it)),
                                SoundCategory.BLOCKS,
                                1F,
                                1F
                            )
                        } catch (e: Exception) {
                            logger.error(e.stackTraceToString())
                        }
                    }

                    drop?.message?.let {
                        if (it.isBlank()) return@let
                        player.sendMessageToClient(Text.of(it), false)
                    }
                }

                chunkManager.removeTicket(ModdedChunkTicketType.AIR_DROP, chunkPos, 1, chunkPos)
            }
        }

        discard()

        return true
    }

    override fun getGravity(): Double = 0.0025

    override fun readCustomDataFromNbt(nbt: NbtCompound) {
        val dropNbt = nbt.getString("drop")
        drop = if (dropNbt.isNullOrBlank()) {
            DatapackLoader.getRandomDrop(world.registryKey.value) ?: run {
                discard()
                return
            }
        } else {
            Drop.fromString(dropNbt)
        }

        logger.debug("=====[ Read NBT ]=====")
        logger.debug(dropNbt)
        logger.debug(drop.toString())
        logger.debug("======================")
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound) {
        logger.debug("=====[ Write NBT ]=====")
        logger.debug(drop.toString())
        logger.debug("=======================")

        if (drop != null) nbt.putString("drop", drop?.toStringJson())
    }


    override fun createSpawnPacket(entityTrackerEntry: EntityTrackerEntry?): Packet<ClientPlayPacketListener> =
        EntitySpawnS2CPacket(this, entityTrackerEntry, Block.getRawIdFromState(BLOCK.defaultState))

    override fun onSpawnPacket(packet: EntitySpawnS2CPacket) {
        super.onSpawnPacket(packet)
        this.setPosition(packet.x, packet.y, packet.z)
    }
}
