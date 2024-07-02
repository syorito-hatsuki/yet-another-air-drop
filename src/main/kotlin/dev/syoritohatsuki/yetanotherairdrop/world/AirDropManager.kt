package dev.syoritohatsuki.yetanotherairdrop.world

import dev.syoritohatsuki.yetanotherairdrop.entity.EntityTypeRegistry
import net.minecraft.entity.SpawnReason
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.random.Random
import net.minecraft.world.GameRules
import net.minecraft.world.Heightmap
import net.minecraft.world.WorldView
import net.minecraft.world.level.ServerWorldProperties
import net.minecraft.world.spawner.SpecialSpawner

class AirDropManager(properties: ServerWorldProperties) : SpecialSpawner {

    companion object {
        private val random: Random = Random.create()
        const val DEFAULT_SPAWN_TIMER: Int = 600
        const val DEFAULT_SPAWN_DELAY: Int = 24000
        const val MIN_SPAWN_CHANCE: Int = 25
        const val MAX_SPAWN_CHANCE: Int = 75
        const val DEFAULT_SPAWN_CHANCE: Int = 25
    }

    private var spawnTimer = 0
    private var spawnDelay = 0
    private var spawnChance = 0

    init {
        spawnTimer = DEFAULT_SPAWN_TIMER
        if (spawnChance == 0) {
            spawnChance = DEFAULT_SPAWN_CHANCE
            properties.wanderingTraderSpawnChance = spawnChance
        }
        if (spawnDelay == 0) {
            spawnDelay = DEFAULT_SPAWN_DELAY
            properties.wanderingTraderSpawnDelay = spawnDelay
        }
    }

    override fun spawn(world: ServerWorld, spawnMonsters: Boolean, spawnAnimals: Boolean): Int {
        when {
            --spawnTimer > 0 -> return 0
            else -> {
                spawnTimer = DEFAULT_SPAWN_TIMER
                spawnDelay -= DEFAULT_SPAWN_TIMER
                spawnDelay = DEFAULT_SPAWN_DELAY
                return when {
                    !world.gameRules.getBoolean(GameRules.DO_MOB_SPAWNING) -> 0
                    else -> {
                        spawnChance =
                            MathHelper.clamp(spawnChance + DEFAULT_SPAWN_CHANCE, MIN_SPAWN_CHANCE, MAX_SPAWN_CHANCE)
                        return when {
                            trySpawn(world) -> {
                                spawnChance = 25
                                1
                            }

                            else -> 0
                        }
                    }
                }
            }
        }
    }

    private fun trySpawn(world: ServerWorld): Boolean {
        val playerEntity: ServerPlayerEntity = world.randomAlivePlayer ?: return true
        val blockPos3: BlockPos? = getNearbySpawnPos(world, playerEntity.blockPos, 48)

        if (blockPos3 != null && this.doesNotSuffocateAt(world, blockPos3)) {
            EntityTypeRegistry.AIR_DROP.spawn(world, blockPos3.up(50), SpawnReason.EVENT) ?: return false
        }

        return false
    }

    private fun getNearbySpawnPos(world: WorldView, pos: BlockPos, range: Int): BlockPos? {
        var blockPos: BlockPos? = null

        repeat(9) {
            val j = pos.x + random.nextInt(range * 2) - range
            val k = pos.z + random.nextInt(range * 2) - range
            val l = world.getTopY(Heightmap.Type.WORLD_SURFACE, j, k)
            val blockPos2 = BlockPos(j, l, k)
            if (world.isAir(blockPos2)) {
                blockPos = blockPos2
                return@repeat
            }
        }

        return blockPos
    }

    private fun doesNotSuffocateAt(world: net.minecraft.world.BlockView, pos: BlockPos): Boolean {
        BlockPos.iterate(pos, pos.add(1, 2, 1)).forEach { blockPos: BlockPos ->
            if (!world.getBlockState(blockPos).getCollisionShape(world, blockPos).isEmpty) return false
        }
        return true
    }
}