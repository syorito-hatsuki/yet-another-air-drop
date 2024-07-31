package dev.syoritohatsuki.yetanotherairdrop.client.render.entity

import dev.syoritohatsuki.yetanotherairdrop.entity.projectile.AirDropEntity
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.block.BlockRenderType
import net.minecraft.block.Blocks
import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.RenderLayers
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.BlockRenderManager
import net.minecraft.client.render.entity.EntityRenderer
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.texture.SpriteAtlasTexture
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.random.Random

@Environment(EnvType.CLIENT)
class AirDropEntityRenderer(context: EntityRendererFactory.Context) : EntityRenderer<AirDropEntity>(context) {

    private var blockRenderManager: BlockRenderManager

    init {
        shadowRadius = 0.5f
        blockRenderManager = context.blockRenderManager
    }

    override fun render(
        fallingBlockEntity: AirDropEntity,
        f: Float,
        g: Float,
        matrixStack: MatrixStack,
        vertexConsumerProvider: VertexConsumerProvider,
        i: Int
    ) {
        val blockState = Blocks.BARREL.defaultState
        if (blockState.renderType == BlockRenderType.MODEL) {
            val world = fallingBlockEntity.world
            if (blockState !== world.getBlockState(fallingBlockEntity.blockPos) && blockState.renderType != BlockRenderType.INVISIBLE) {
                matrixStack.push()
                val blockPos =
                    BlockPos.ofFloored(fallingBlockEntity.x, fallingBlockEntity.boundingBox.maxY, fallingBlockEntity.z)
                matrixStack.translate(-0.5, 0.0, -0.5)
                this.blockRenderManager
                    .modelRenderer
                    .render(
                        world,
                        this.blockRenderManager.getModel(blockState),
                        blockState,
                        blockPos,
                        matrixStack,
                        vertexConsumerProvider.getBuffer(RenderLayers.getMovingBlockLayer(blockState)),
                        false,
                        Random.create(),
                        blockState.getRenderingSeed(fallingBlockEntity.blockPos),
                        OverlayTexture.DEFAULT_UV
                    )
                matrixStack.pop()
                super.render(fallingBlockEntity, f, g, matrixStack, vertexConsumerProvider, i)
            }
        }
    }

    override fun getTexture(airDropEntity: AirDropEntity): Identifier = SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE
}

