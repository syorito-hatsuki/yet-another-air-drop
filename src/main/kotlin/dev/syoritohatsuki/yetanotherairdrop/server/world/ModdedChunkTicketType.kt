package dev.syoritohatsuki.yetanotherairdrop.server.world

import net.minecraft.server.world.ChunkTicketType
import net.minecraft.util.math.ChunkPos

object ModdedChunkTicketType {
    val AIR_DROP: ChunkTicketType<ChunkPos> = ChunkTicketType.create(
        "air_drop", Comparator.comparingLong { chunkPos: ChunkPos ->
            chunkPos.toLong()
        }, 40
    )
}
