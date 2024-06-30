package dev.syoritohatsuki.yetanotherairdrop.world

import dev.syoritohatsuki.yetanotherairdrop.YetAnotherAirDrop.logger
import dev.syoritohatsuki.yetanotherairdrop.entity.EntityTypeRegistry
import net.minecraft.entity.SpawnReason
import net.minecraft.entity.SpawnRestriction
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.random.Random
import net.minecraft.world.GameRules
import net.minecraft.world.Heightmap
import net.minecraft.world.WorldView
import net.minecraft.world.level.ServerWorldProperties
import net.minecraft.world.poi.PointOfInterestStorage
import net.minecraft.world.poi.PointOfInterestType
import net.minecraft.world.poi.PointOfInterestTypes
import net.minecraft.world.spawner.SpecialSpawner

class AirDropManager(private val properties: ServerWorldProperties) : SpecialSpawner {

    companion object {
        private const val LOGGER_PREFIX = "AirDropManager\$"
        private val random: Random = Random.create()
        const val DEFAULT_SPAWN_TIMER: Int = 200
        const val DEFAULT_SPAWN_DELAY: Int = 24000
        const val MIN_SPAWN_CHANCE: Int = 25
        const val MAX_SPAWN_CHANCE: Int = 75
        const val DEFAULT_SPAWN_CHANCE: Int = 25
    }

    private var spawnTimer = 0
    private var spawnDelay = 0
    private var spawnChance = 0

    init {
        spawnTimer = 1200
        spawnDelay = properties.wanderingTraderSpawnDelay
        spawnChance = properties.wanderingTraderSpawnChance

        if (spawnChance == 0) {
            spawnChance = 25
            properties.wanderingTraderSpawnChance = spawnChance
        }
        if (spawnDelay == 0) {
            spawnDelay = 24000
            properties.wanderingTraderSpawnDelay = spawnDelay
        }
    }

    override fun spawn(world: ServerWorld, spawnMonsters: Boolean, spawnAnimals: Boolean): Int {
        logger.info("${LOGGER_PREFIX}spawn call")
        if (--spawnTimer > 0) {
            logger.info("${LOGGER_PREFIX}spawn if (--spawnTimer > 0 | ${spawnTimer})")
            return 0
        } else {
            logger.info("${LOGGER_PREFIX}spawn else 1")
            spawnTimer = DEFAULT_SPAWN_TIMER
            spawnDelay -= DEFAULT_SPAWN_TIMER
            properties.wanderingTraderSpawnDelay = spawnDelay
//            if (spawnDelay > 0) {
//                logger.info("${LOGGER_PREFIX}spawn if(spawnDelay > 0) | $spawnDelay")
//                return 0
//            } else {
                logger.info("${LOGGER_PREFIX}spawn else 2")
                spawnDelay = DEFAULT_SPAWN_DELAY
                if (!world.gameRules.getBoolean(GameRules.DO_MOB_SPAWNING)) {
                    return 0
                } else {
                    logger.info("${LOGGER_PREFIX}spawn else 3")
                    val i = spawnChance
                    spawnChance = MathHelper.clamp(spawnChance + DEFAULT_SPAWN_CHANCE, MIN_SPAWN_CHANCE, MAX_SPAWN_CHANCE)
                    properties.wanderingTraderSpawnChance = spawnChance
                    val ddd = random.nextInt(100)
//                    if (ddd > i) {
//                        logger.info("${LOGGER_PREFIX}spawn if () | $ddd")
//                        return 0
                    /*} else*/ if (trySpawn(world)) {
                        logger.info("${LOGGER_PREFIX}spawn trySpawn")
                        spawnChance = 25
                        return 1
                    } else {
                        logger.info("${LOGGER_PREFIX}spawn else 4")
                        return 0
                    }
//                }
            }
        }
    }

    private fun trySpawn(world: ServerWorld): Boolean {
        val playerEntity: PlayerEntity? = world.randomAlivePlayer
        if (playerEntity == null) {
            logger.info("${LOGGER_PREFIX}trySpawn player null :(")
            return true
//        } else if (random.nextInt(10) != 0) {
//            logger.info("${LOGGER_PREFIX}trySpawn random miss")
//            return false
        } else {
            logger.info("${LOGGER_PREFIX}trySpawn TRY")
            val blockPos = playerEntity.blockPos
            val optional = world.pointOfInterestStorage.getPosition({ poiType: RegistryEntry<PointOfInterestType> ->
                poiType.matchesKey(PointOfInterestTypes.MEETING)
            }, { _: BlockPos? -> true }, blockPos, 48, PointOfInterestStorage.OccupationStatus.ANY
            )
            val blockPos3: BlockPos? = getNearbySpawnPos(world, optional.orElse(blockPos) as BlockPos, 48)
            if (blockPos3 != null && this.doesNotSuffocateAt(world, blockPos3)) {
                logger.info("${LOGGER_PREFIX}trySpawn SPAWN")
                val airDropEntity = EntityTypeRegistry.AIR_DROP.spawn(world, blockPos3, SpawnReason.EVENT)
                if (airDropEntity != null) return true
            }
            logger.info("${LOGGER_PREFIX}trySpawn not suffocate")
            return false
        }
    }

    private fun getNearbySpawnPos(world: WorldView, pos: BlockPos, range: Int): BlockPos? {
        var blockPos: BlockPos? = null
        val spawnLocation = SpawnRestriction.getLocation(EntityTypeRegistry.AIR_DROP)

        for (i in 0..9) {
            val j = pos.x + random.nextInt(range * 2) - range
            val k = pos.z + random.nextInt(range * 2) - range
            val l = world.getTopY(Heightmap.Type.WORLD_SURFACE, j, k)
            val blockPos2 = BlockPos(j, l, k)
            if (spawnLocation.isSpawnPositionOk(world, blockPos2, EntityTypeRegistry.AIR_DROP)) {
                blockPos = blockPos2
                break
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