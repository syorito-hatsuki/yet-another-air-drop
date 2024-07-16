package dev.syoritohatsuki.yetanotherairdrop.dto

import kotlinx.serialization.Serializable

@Serializable
data class Drop(
    val name: String,
    val message: String? = null,
    val sound: String? = null,
    val lootTables: Set<String> = emptySet()
)
