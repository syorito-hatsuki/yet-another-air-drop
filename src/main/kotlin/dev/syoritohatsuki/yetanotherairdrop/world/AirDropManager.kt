package dev.syoritohatsuki.yetanotherairdrop.world

import dev.syoritohatsuki.yetanotherairdrop.DatapackLoader
import dev.syoritohatsuki.yetanotherairdrop.YetAnotherAirDrop.logger
import dev.syoritohatsuki.yetanotherairdrop.entity.projectile.AirDropEntity
import net.minecraft.block.Blocks
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.random.Random
import net.minecraft.world.Heightmap
import net.minecraft.world.WorldView
import net.minecraft.world.spawner.SpecialSpawner

class AirDropManager : SpecialSpawner {

    companion object {
        const val DEFAULT_SPAWN_TIMER: Int = 600

        val random: Random = Random.create()

        private var instanceCount = 0
    }

    private var spawnTimer = 0

    init {
        logger.debug("Instance count: ${++instanceCount}. It should be 1, but may can bigger :D")
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

        logger.debug("----[ Entities: ${listOfPlayer.size}]----")
        val playerEntity = listOfPlayer.onEach {
            logger.debug(it.name?.literalString)
        }.random()
        logger.debug("--------------------")

        val playerWorld = playerEntity.world
        val blockPos3: BlockPos = getNearbySpawnPos(playerWorld, playerEntity.blockPos, 48) ?: return 0

        logger.debug("Try to spawn Air Drop in: ${playerWorld.registryKey.value} at ${blockPos3.toShortString()}")

        if (!playerWorld.spawnEntity(AirDropEntity(playerWorld, blockPos3.x.toDouble(), blockPos3.y.toDouble(), blockPos3.z.toDouble()))) {
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
                logger.debug("Is air: ${world.getBlockState(pos)} | ${pos.x}, ${pos.y}, ${pos.z}")
                return pos
            }
        }
        return null
    }

    private fun findFirstSolid(start: Int, pos: BlockPos.Mutable, world: WorldView): BlockPos.Mutable? {
        for (y in start downTo 0) {
            pos.y = y
            if (world.getBlockState(pos).isSolidBlock(world, pos)) {
                logger.debug("Is solid: ${world.getBlockState(pos)} | ${pos.x}, ${pos.y}, ${pos.z}")
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
        val halfWorldY = world.topY / 2

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
}