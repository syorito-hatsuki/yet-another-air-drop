package dev.syoritohatsuki.yetanotherairdrop.client

import dev.syoritohatsuki.yetanotherairdrop.client.render.entity.EntityRenderersRegistry
import net.fabricmc.api.ClientModInitializer

object YetAnotherAirDropClient : ClientModInitializer {
    override fun onInitializeClient() {
        EntityRenderersRegistry
    }
}