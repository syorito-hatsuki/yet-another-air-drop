package dev.syoritohatsuki.yetanotherairdrop.client.render.entity

import dev.syoritohatsuki.yetanotherairdrop.entity.projectile.AirDropEntity
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.EntityRenderer
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper

@Environment(EnvType.CLIENT)
class AirDropEntityRenderer(context: EntityRendererFactory.Context) : EntityRenderer<AirDropEntity>(context) {

    companion object {
        val TEXTURE: Identifier = Identifier.ofVanilla("textures/entity/experience_orb.png")
        val LAYER: RenderLayer = RenderLayer.getItemEntityTranslucentCull(TEXTURE)
    }

    init {
        shadowRadius = 0.15f
        shadowOpacity = 0.75f
    }

    override fun getBlockLight(airDropEntity: AirDropEntity, blockPos: BlockPos): Int =
        MathHelper.clamp(super.getBlockLight(airDropEntity, blockPos) + 7, 0, 15)

    override fun render(
        airDropEntity: AirDropEntity,
        f: Float,
        g: Float,
        matrixStack: MatrixStack,
        vertexConsumerProvider: VertexConsumerProvider,
        i: Int
    ) {
        matrixStack.push()
        val j = 1
        val h = (j % 4 * 16 + 0).toFloat() / 64.0f
        val k = (j % 4 * 16 + 16).toFloat() / 64.0f
        val l = (j / 4 * 16 + 0).toFloat() / 64.0f
        val m = (j / 4 * 16 + 16).toFloat() / 64.0f
        val r = (airDropEntity.age.toFloat() + g) / 2.0f
        val s = ((MathHelper.sin(r + 0.0f) + 1.0f) * 0.5f * 255.0f).toInt()
        val u = ((MathHelper.sin(r + (Math.PI * 4.0 / 3.0).toFloat()) + 1.0f) * 0.1f * 255.0f).toInt()
        val a = ((MathHelper.sin(r + (Math.E * 4.0 / 3.0).toFloat()) + 1.0f) * 0.1f * 255.0f).toInt()
        matrixStack.translate(0.0f, 0.1f, 0.0f)
        matrixStack.multiply(dispatcher.rotation)
        matrixStack.scale(0.3f, 0.3f, 0.3f)
        val vertexConsumer = vertexConsumerProvider.getBuffer(LAYER)
        val entry = matrixStack.peek()
        vertex(vertexConsumer, entry, -0.5f, -0.25f, s, a, u, h, m, i)
        vertex(vertexConsumer, entry, 0.5f, -0.25f, s, a, u, k, m, i)
        vertex(vertexConsumer, entry, 0.5f, 0.75f, s, a, u, k, l, i)
        vertex(vertexConsumer, entry, -0.5f, 0.75f, s, a, u, h, l, i)
        matrixStack.pop()
        super.render(airDropEntity, f, g, matrixStack, vertexConsumerProvider, i)
    }

    private fun vertex(
        vertexConsumer: VertexConsumer,
        matrix: MatrixStack.Entry,
        x: Float,
        y: Float,
        red: Int,
        green: Int,
        blue: Int,
        u: Float,
        v: Float,
        light: Int
    ) {
        vertexConsumer.vertex(matrix, x, y, 0.0f)
            .color(red, green, blue, 128)
            .texture(u, v)
            .overlay(OverlayTexture.DEFAULT_UV)
            .light(light)
            .normal(matrix, 0.0f, 1.0f, 0.0f)
    }

    override fun getTexture(airDropEntity: AirDropEntity): Identifier = TEXTURE
}

