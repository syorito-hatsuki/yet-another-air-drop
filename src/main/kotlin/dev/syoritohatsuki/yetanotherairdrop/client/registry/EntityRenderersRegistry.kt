package dev.syoritohatsuki.yetanotherairdrop.client.registry

import dev.syoritohatsuki.yetanotherairdrop.client.render.entity.AirDropEntityRenderer
import dev.syoritohatsuki.yetanotherairdrop.entity.EntityTypeRegistry
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry

object EntityRenderersRegistry {
    init {
        EntityRendererRegistry.register(EntityTypeRegistry.AIR_DROP, ::AirDropEntityRenderer)
    }
}