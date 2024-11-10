package dev.syoritohatsuki.yetanotherairdrop.client.registry

import dev.syoritohatsuki.yetanotherairdrop.YetAnotherAirDrop
import dev.syoritohatsuki.yetanotherairdrop.client.render.entity.model.AirDropEntityModel
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry
import net.minecraft.client.render.entity.model.EntityModelLayer
import net.minecraft.util.Identifier

object EntityModelsLayerRegistry {

    val MODEL_AIR_DROP_LAYER = EntityModelLayer(Identifier.of(YetAnotherAirDrop.MOD_ID, "textures/entity/airdrop.png"),"root")

    init {
        EntityModelLayerRegistry.registerModelLayer(
            MODEL_AIR_DROP_LAYER,
            AirDropEntityModel::getTexturedModelData
        )
    }
}