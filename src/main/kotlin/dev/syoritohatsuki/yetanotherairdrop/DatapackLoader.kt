package dev.syoritohatsuki.yetanotherairdrop

import dev.syoritohatsuki.yetanotherairdrop.YetAnotherAirDrop.MOD_ID
import dev.syoritohatsuki.yetanotherairdrop.YetAnotherAirDrop.logger
import dev.syoritohatsuki.yetanotherairdrop.dto.Drop
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener
import net.minecraft.resource.ResourceManager
import net.minecraft.resource.ResourceType
import net.minecraft.server.MinecraftServer
import net.minecraft.util.Identifier

object DatapackLoader {
    private val json = Json {
        encodeDefaults = true
        ignoreUnknownKeys = true
        prettyPrint = true
    }

    private val worlds = mutableMapOf<String, Set<Drop>>()

    @OptIn(ExperimentalSerializationApi::class)
    fun register(server: MinecraftServer) {
        logger.info("Registering Air Drop Datapacks")
        ResourceManagerHelper.get(ResourceType.SERVER_DATA)
            .registerReloadListener(object : SimpleSynchronousResourceReloadListener {
                override fun reload(manager: ResourceManager) {
                    logger.debug("$MOD_ID datapack reloaded")
                    worlds.clear()
                    server.worldRegistryKeys.forEach { world ->
                        logger.debug("-----[ N: ${world.value.namespace} P: ${world.value.path} ]-----")
                        manager.findResources("${world.value.namespace}/${world.value.path}") { identifier ->
                            logger.debug("Path: {}", identifier)
                            try {
                                manager.getResourceOrThrow(identifier).inputStream.use {
                                    refresh(world.value.toString(), json.decodeFromStream(it))
                                }
                                true
                            } catch (e: Exception) {
                                logger.error("Error occurred while loading resource json $identifier", e)
                                false
                            }
                        }
                        logger.debug("--------------------------------------")
                    }
                }

                override fun getFabricId(): Identifier = Identifier.of(MOD_ID, "air_drop")
            })
    }

    private fun refresh(worldIdentifierString: String, drop: Drop) {
        worlds[worldIdentifierString] = worlds.getOrPut(worldIdentifierString) { emptySet() } + drop
    }

    fun worldsWhereDropExist(): Set<String> = worlds.keys

    fun getRandomDrop(worldIdentifier: Identifier) = worlds[worldIdentifier.toString()]?.random()

    fun getRandomLootTable(drop: Drop) = drop.lootTables.random()
}