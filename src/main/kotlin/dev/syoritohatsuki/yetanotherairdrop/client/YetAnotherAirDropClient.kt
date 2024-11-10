package dev.syoritohatsuki.yetanotherairdrop.client

import dev.syoritohatsuki.yetanotherairdrop.client.registry.EntityModelsLayerRegistry
import dev.syoritohatsuki.yetanotherairdrop.client.registry.EntityRenderersRegistry
import net.fabricmc.api.ClientModInitializer

object YetAnotherAirDropClient : ClientModInitializer {
    override fun onInitializeClient() {
        EntityRenderersRegistry
        EntityModelsLayerRegistry
    }
}