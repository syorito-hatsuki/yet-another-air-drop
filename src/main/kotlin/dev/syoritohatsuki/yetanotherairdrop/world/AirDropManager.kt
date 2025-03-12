package dev.syoritohatsuki.yetanotherairdrop.world

import dev.syoritohatsuki.yetanotherairdrop.ConfigManager
import dev.syoritohatsuki.yetanotherairdrop.DatapackLoader
import dev.syoritohatsuki.yetanotherairdrop.YetAnotherAirDrop.logger
import dev.syoritohatsuki.yetanotherairdrop.entity.projectile.AirDropEntity
import dev.syoritohatsuki.yetanotherairdrop.server.world.ModdedChunkTicketType
import net.minecraft.block.Blocks
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnReason
import net.minecraft.nbt.*
import net.minecraft.server.MinecraftServer
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Identifier
import net.minecraft.util.WorldSavePath
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import net.minecraft.util.math.random.Random
import net.minecraft.world.Heightmap
import net.minecraft.world.World
import net.minecraft.world.WorldView
import net.minecraft.world.spawner.SpecialSpawner
import kotlin.io.path.createFile
import kotlin.io.path.notExists

class AirDropManager(private val server: MinecraftServer) : SpecialSpawner {

    companion object {

        private const val AIR_DROPS_KEY = "air_drops"
        private const val AIR_DROP_DIMENSION_KEY = "air_drop_dimension"

        private val airDrops: MutableSet<AirDropEntity> = mutableSetOf()
        private val random: Random = Random.create()

        private var instanceCount = 0

        fun addAirDropTicket(world: ServerWorld, chunkPos: ChunkPos) {
            world.chunkManager.addTicket(ModdedChunkTicketType.AIR_DROP, chunkPos, 2, chunkPos)
        }

        fun handleBarrelFly(airDropEntity: AirDropEntity): Long {
            if (airDropEntity.world !is ServerWorld) return 0L

            val serverWorld = (airDropEntity.world as ServerWorld)
            airDrops.add(airDropEntity)
            serverWorld.resetIdleTimeout()
            addAirDropTicket(serverWorld, airDropEntity.chunkPos)
            return ModdedChunkTicketType.AIR_DROP.expiryTicks - 1L
        }
    }

    private val airDropsPath = server.getSavePath(WorldSavePath.ROOT).resolve("airdrops.dat").apply {
        if (notExists()) {
            createFile()
            NbtIo.write(NbtCompound(), this)
        }
    }
    private val airDropsNbt = NbtIo.read(airDropsPath) ?: NbtCompound()

    private var spawnTimer = 0

    init {
        logger.debug("Instance count: ${++instanceCount}. It should be 1, but may can bigger :D")
        spawnTimer = ConfigManager.read().delay
    }

    override fun spawn(world: ServerWorld, spawnMonsters: Boolean, spawnAnimals: Boolean): Int {
        if (--spawnTimer > 0) return 0
        spawnTimer = ConfigManager.read().delay
        return trySpawn(world)
    }

    private fun trySpawn(world: ServerWorld): Int {
        val listOfPlayer = world.server.worlds.filter {
            DatapackLoader.worldsWhereDropExist().contains(it.registryKey.value.toString())
        }.mapNotNull {
            it.randomAlivePlayer
        }

        if (listOfPlayer.isEmpty()) return 0

        logger.debug("----[ Entities: ${listOfPlayer.size}]----")
        val playerEntity = listOfPlayer.onEach {
            logger.debug(it.name?.literalString)
        }.random()
        logger.debug("--------------------")

        val playerWorld = playerEntity.world
        val blockPos3: BlockPos = getNearbySpawnPos(playerWorld, playerEntity.blockPos, 48) ?: return 0

        logger.debug(
            "Try to spawn Air Drop in: {} at {}", playerWorld.registryKey.value, blockPos3.toShortString()
        )

        if (!playerWorld.spawnEntity(
                AirDropEntity(
                    playerWorld, blockPos3.x.toDouble(), blockPos3.y.toDouble(), blockPos3.z.toDouble()
                )
            )
        ) {
            logger.debug("Can't spawn Entity")
            return 0
        }
        logger.debug("Air Drop spawn: Success")
        return 1
    }

    private fun findFirstAir(start: Int, pos: BlockPos.Mutable, world: WorldView): BlockPos.Mutable? {
        for (y in start downTo 0) {
            pos.y = y
            if (world.getBlockState(pos).isOf(Blocks.AIR)) {
                logger.debug("Is air: {} | {}, {}, {}", world.getBlockState(pos), pos.x, pos.y, pos.z)
                return pos
            }
        }
        return null
    }

    private fun findFirstSolid(start: Int, pos: BlockPos.Mutable, world: WorldView): BlockPos.Mutable? {
        for (y in start downTo 0) {
            pos.y = y
            if (world.getBlockState(pos).isSolidBlock(world, pos)) {
                logger.debug("Is solid: {} | {}, {}, {}", world.getBlockState(pos), pos.x, pos.y, pos.z)
                return pos
            }
        }
        return null
    }

    private fun getSurfaceForDimensionWithRoof(world: WorldView, x: Int, halfWorldY: Int, z: Int): Int {
        val solid = findFirstSolid(halfWorldY, BlockPos.Mutable(x, halfWorldY, z), world) ?: return -1
        return findFirstAir(solid.y, solid, world)?.y ?: -1
    }

    private fun getNearbySpawnPos(world: WorldView, pos: BlockPos, range: Int): BlockPos? {
        val halfWorldY = world.topYInclusive / 2

        repeat(9) {
            val randomX = pos.x + random.nextInt(range * 2) - range
            val randomZ = pos.z + random.nextInt(range * 2) - range
            var topY = world.getTopY(Heightmap.Type.WORLD_SURFACE, randomX, randomZ)
            logger.debug("$topY >= $halfWorldY = ${topY >= halfWorldY}")

            val surface = getSurfaceForDimensionWithRoof(world, randomX, halfWorldY, randomZ)
            logger.debug("Surface: $surface")

            if (topY >= halfWorldY) topY = surface
            else topY += random.nextBetween(30, 50)
            logger.debug("Checking ${randomX}, ${topY}, $randomZ")

            val blockPos2 = BlockPos(randomX, topY, randomZ)

            if (world.isAir(blockPos2)) {
                logger.debug("Nearly spawn found on: ${blockPos2.toShortString()}")
                return blockPos2
            }
        }

        logger.debug("Can't spawn near: ${pos.toShortString()}")
        return null
    }

    fun writeAirDrops() {
        if (airDrops.isEmpty()) return

        airDropsNbt.put(AIR_DROPS_KEY, NbtList().apply {
            airDrops.forEach { airDrop ->
                if (airDrop.isRemoved) {
                    logger.warn("Trying to save removed air drop, skipping")
                    return@forEach
                }

                add(NbtCompound().apply {
                    airDrop.saveNbt(this)
                    Identifier.CODEC.encodeStart(NbtOps.INSTANCE, airDrop.world.registryKey.value)
                        .resultOrPartial { msg: String? -> logger.error(msg) }
                        .ifPresent { put(AIR_DROP_DIMENSION_KEY, it) }
                })
            }
        })

        NbtIo.write(airDropsNbt, airDropsPath)
    }

    fun readAirDrops() {
        if (airDropsNbt.contains(
                AIR_DROPS_KEY, NbtElement.LIST_TYPE.toInt()
            ) && airDropsNbt.get(AIR_DROPS_KEY) is NbtList
        ) {
            (airDropsNbt.get(AIR_DROPS_KEY) as NbtList).forEach { nbtElement ->
                if (nbtElement is NbtCompound && nbtElement.contains(AIR_DROP_DIMENSION_KEY)) {
                    val optional = World.CODEC.parse(NbtOps.INSTANCE, nbtElement[AIR_DROP_DIMENSION_KEY])
                        .resultOrPartial(logger::error)

                    if (optional.isEmpty) {
                        logger.warn("No dimension defined for air drop, skipping")
                        return@forEach
                    }

                    val serverWorld: ServerWorld = server.getWorld(optional.get()) ?: run {
                        logger.warn("Trying to load air drop without level ({}) being loaded, skipping", optional.get())
                        return@forEach
                    }

                    val airDropEntity = EntityType.loadEntityWithPassengers(nbtElement, serverWorld, SpawnReason.LOAD) {
                        when {
                            !serverWorld.tryLoadEntity(it) -> null
                            else -> it
                        }
                    } ?: run {
                        logger.warn("Failed to spawn player air drop in level ({}), skipping", optional.get())
                        return@forEach
                    }

                    addAirDropTicket(serverWorld, airDropEntity.chunkPos)
                }
            }
        }
    }
}
