package dev.syoritohatsuki.yetanotherairdrop.world

import dev.syoritohatsuki.yetanotherairdrop.DatapackLoader
import dev.syoritohatsuki.yetanotherairdrop.YetAnotherAirDrop
import dev.syoritohatsuki.yetanotherairdrop.entity.projectile.AirDropEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.random.Random
import net.minecraft.world.BlockView
import net.minecraft.world.Heightmap
import net.minecraft.world.WorldView
import net.minecraft.world.spawner.SpecialSpawner

class AirDropManager : SpecialSpawner {

    companion object {
        const val DEFAULT_SPAWN_TIMER: Int = 600

        private val random: Random = Random.create()

        private var instanceCount = 0
    }

    private var spawnTimer = 0

    init {
        YetAnotherAirDrop.logger.warn("Instance count: ${++instanceCount}. It should be 1, but may can bigger :D")
        spawnTimer = DEFAULT_SPAWN_TIMER
    }

    override fun spawn(world: ServerWorld, spawnMonsters: Boolean, spawnAnimals: Boolean): Int {
        if (--spawnTimer > 0) return 0
        spawnTimer = DEFAULT_SPAWN_TIMER
        return trySpawn(world)
    }

    private fun trySpawn(world: ServerWorld): Int {
        val listOfPlayer = world.server.worlds.filter {
            DatapackLoader.worldsWhereDropExist().contains(it.registryKey.value.toString())
        }.mapNotNull {
            it.randomAlivePlayer
        }

        if (listOfPlayer.isEmpty()) return 0

        YetAnotherAirDrop.logger.warn("----[ Entities: ${listOfPlayer.size}]----")
        val playerEntity = listOfPlayer.onEach {
            YetAnotherAirDrop.logger.warn(it.name?.literalString)
        }.random()
        YetAnotherAirDrop.logger.warn("--------------------")

        val playerWorld = playerEntity.world
        val blockPos3: BlockPos? = getNearbySpawnPos(playerWorld, playerEntity.blockPos, 48)

        YetAnotherAirDrop.logger.warn("Try to spawn Air Drop in: ${playerWorld.registryKey.value} at ${blockPos3?.toShortString()}")

        if (blockPos3 != null && this.doesNotSuffocateAt(playerEntity.world, blockPos3)) {
            YetAnotherAirDrop.logger.warn("Air Drop spawn: Success")
            val height = random.nextBetween(10, 100)
            val upBlockPos = blockPos3.up(height).toCenterPos()

            if (!playerWorld.spawnEntity(
                    AirDropEntity(playerWorld,
                        upBlockPos.x,
                        upBlockPos.y,
                        upBlockPos.z,
                        DatapackLoader.getRandomDrop(playerWorld.registryKey.value) ?: run {
                            YetAnotherAirDrop.logger.warn("Drop is null o_O")
                            return 0
                        })
                )
            ) {
                YetAnotherAirDrop.logger.warn("Can't spawn Entity")
                return 0
            }
            return 1
        }

        YetAnotherAirDrop.logger.warn("Air Drop spawn: Unsuccessful")

        return 0
    }

    private fun getNearbySpawnPos(world: WorldView, pos: BlockPos, range: Int): BlockPos? {
        repeat(9) {
            val randomX = pos.x + random.nextInt(range * 2) - range
            val randomZ = pos.z + random.nextInt(range * 2) - range
            val topY = world.getTopY(Heightmap.Type.WORLD_SURFACE, randomX, randomZ)

            YetAnotherAirDrop.logger.warn("Checking ${randomX}, ${topY}, $randomZ")

            val blockPos2 = BlockPos(randomX, topY, randomZ)
            if (world.isAir(blockPos2)) {
                YetAnotherAirDrop.logger.warn("Nearly spawn found on: ${blockPos2.toShortString()}")
                return blockPos2
            }
        }

        YetAnotherAirDrop.logger.warn("Can't spawn near: ${pos.toShortString()}")
        return null
    }

    private fun doesNotSuffocateAt(world: BlockView, pos: BlockPos): Boolean {
        BlockPos.iterate(pos, pos.add(1, 2, 1)).forEach { blockPos: BlockPos ->
            if (!world.getBlockState(blockPos).getCollisionShape(world, blockPos).isEmpty) {
                YetAnotherAirDrop.logger.warn("Not suffocate: ${blockPos.toShortString()}")
                return false
            }
        }
        YetAnotherAirDrop.logger.warn("Suffocate: ${pos.toShortString()}")
        return true
    }
}