package dev.syoritohatsuki.yetanotherairdrop.client.render.entity

import dev.syoritohatsuki.yetanotherairdrop.YetAnotherAirDrop
import dev.syoritohatsuki.yetanotherairdrop.client.registry.EntityModelsLayerRegistry
import dev.syoritohatsuki.yetanotherairdrop.client.render.entity.model.AirDropEntityModel
import dev.syoritohatsuki.yetanotherairdrop.entity.projectile.AirDropEntity
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.EntityRenderer
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.render.entity.state.EntityRenderState
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier

@Environment(EnvType.CLIENT)
class AirDropEntityRenderer(context: EntityRendererFactory.Context) :
    EntityRenderer<AirDropEntity, EntityRenderState>(context) {

    private var model: AirDropEntityModel = AirDropEntityModel(
        context.getPart(EntityModelsLayerRegistry.MODEL_AIR_DROP_LAYER)
    )

    init {
        shadowRadius = 0.5f
    }

    override fun render(
        renderState: EntityRenderState,
        matrixStack: MatrixStack,
        vertexConsumerProvider: VertexConsumerProvider,
        light: Int
    ) {
        model.render(
            matrixStack, vertexConsumerProvider.getBuffer(
                RenderLayer.getEntitySolid(
                    Identifier.of(YetAnotherAirDrop.MOD_ID, "textures/entity/airdrop.png")
                )
            ), light, OverlayTexture.DEFAULT_UV
        )
    }

    override fun createRenderState(): EntityRenderState = EntityRenderState()
}

