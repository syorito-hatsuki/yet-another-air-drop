package dev.syoritohatsuki.yetanotherairdrop.dto

import kotlinx.serialization.Serializable

@Serializable
data class Drop(
    val name: String,
    val message: String? = null,
    val sound: String? = null,
    // Under question. Maybe will be removed and replaced with auto search
    val heightLimit: Int? = null,
    val safePlatform: Boolean = false,
    val lootTables: Set<String> = emptySet()
)
