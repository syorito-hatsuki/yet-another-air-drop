package dev.syoritohatsuki.yetanotherairdrop.entity.projectile

import dev.syoritohatsuki.yetanotherairdrop.DatapackLoader
import dev.syoritohatsuki.yetanotherairdrop.YetAnotherAirDrop.logger
import dev.syoritohatsuki.yetanotherairdrop.dto.Drop
import dev.syoritohatsuki.yetanotherairdrop.entity.EntityTypeRegistry
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.entity.BarrelBlockEntity
import net.minecraft.component.DataComponentTypes
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.MovementType
import net.minecraft.entity.data.DataTracker
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.Registries
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.sound.SoundCategory
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import net.minecraft.world.explosion.Explosion

class AirDropEntity(type: EntityType<AirDropEntity>, world: World) : Entity(type, world) {

    private var drop: Drop? = null

    constructor(world: World, x: Double, y: Double, z: Double, drop: Drop) : this(EntityTypeRegistry.AIR_DROP, world) {
        this.drop = drop
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

    override fun isGlowing(): Boolean = true

    override fun onBlockCollision(state: BlockState) {
        super.onBlockCollision(state)

        if (drop == null) return

        if (blockY == world.bottomY) {
            if (!drop!!.safePlatform) {
                discard()
                return
            }

            for (offsetX in -1..1) {
                for (offsetZ in -1..1) {
                    world.setBlockState(
                        BlockPos(x.toInt() + offsetX, y.toInt(), z.toInt() + offsetZ),
                        Blocks.GLASS.defaultState,
                        Block.NOTIFY_ALL_AND_REDRAW
                    )
                }
            }

            trySetBarrel(state, blockPos.up().offset(Direction.Axis.X, 1).offset(Direction.Axis.Z, 1))
        }

        if (!isOnGround) return

        if (trySetBarrel(state, blockPos)) return

        val newPos = blockPos.up()
        if (trySetBarrel(world.getBlockState(newPos), newPos)) return
    }

    private fun trySetBarrel(state: BlockState, blockPos: BlockPos): Boolean {
        if (!state.isReplaceable)

        if (world.setBlockState(blockPos, Blocks.BARREL.defaultState, Block.NOTIFY_ALL_AND_REDRAW)) {

            val randomTable = DatapackLoader.getRandomLootTable(drop!!)
            logger.info("Random table: $randomTable")

            val blockEntity = world.getBlockEntity(blockPos) as BarrelBlockEntity

            val verifiedLootTable = server?.reloadableRegistries?.getIds(RegistryKeys.LOOT_TABLE)?.firstOrNull {
                it.toString() == randomTable
            }

            if (verifiedLootTable != null) {
                blockEntity.lootTable = RegistryKey.of(RegistryKeys.LOOT_TABLE, verifiedLootTable)
            } else {
                blockEntity.setStack(13, ItemStack(Items.PAPER).apply {
                    set(
                        DataComponentTypes.CUSTOM_NAME,
                        Text.literal("ERROR! Loot table $randomTable not exist").styled {
                            it.withBold(true).withColor(Formatting.RED)
                        })
                })
            }

        }

        discard()

        drop?.sound?.let {
            if (it.isBlank()) return@let
            logger.warn("Play: $it")
            try {
                world.playSound(
                    this, blockPos, Registries.SOUND_EVENT.get(Identifier.of(it)), SoundCategory.BLOCKS, 1F, 1F
                )
            } catch (e: Exception) {
                logger.error(e.stackTraceToString())
            }
        }

        drop?.message?.let {
            if (it.isBlank()) return@let
            logger.warn("Message: $it")
            server?.playerManager?.playerList?.forEach { entity ->
                entity.sendMessageToClient(Text.of(it), false)
            }
        }

        return true
    }

    override fun tick() {
        super.tick()
        applyGravity()

        move(MovementType.SELF, velocity)
        var force = 0.98f
        if (isOnGround) force = world.getBlockState(velocityAffectingPos).block.slipperiness * 0.98f

        velocity = velocity.multiply(force.toDouble(), 0.98, force.toDouble())
        if (isOnGround) velocity = velocity.multiply(1.0, -0.9, 1.0)
    }

    override fun getGravity(): Double = 0.005

    override fun initDataTracker(builder: DataTracker.Builder) {
    }

    override fun readCustomDataFromNbt(nbt: NbtCompound) {
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound) {
    }
}