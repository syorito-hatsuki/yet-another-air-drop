package dev.syoritohatsuki.yetanotherairdrop.dto

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class Drop(
    val name: String,
    val message: String? = null,
    val sound: String? = null,
    val safePlatform: Boolean = false,
    val lootTables: Set<String> = emptySet()
) {
    companion object {
        fun fromString(string: String): Drop = Json.decodeFromString(string)
    }

    fun toStringJson(): String = Json.encodeToString(this)
}
