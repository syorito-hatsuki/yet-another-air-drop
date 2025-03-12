package dev.syoritohatsuki.yetanotherairdrop

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.fabricmc.loader.api.FabricLoader
import java.io.File

object ConfigManager {
    private val configDir: File = FabricLoader.getInstance().configDir.resolve(YetAnotherAirDrop.MOD_ID).toFile()
    private val configFile = File(configDir, "config.json")

    private val clientConfigJson = Json {
        encodeDefaults = true
        ignoreUnknownKeys = true
        prettyPrint = true
    }

    init {
        if (!configDir.exists()) configDir.mkdirs()
        if (!configFile.exists()) configFile.writeText(clientConfigJson.encodeToString(Config()))
    }

    fun read() = clientConfigJson.decodeFromString<Config>(configFile.readText())

    @Serializable
    data class Config(
        val delay: Int = 24000
    )
}
