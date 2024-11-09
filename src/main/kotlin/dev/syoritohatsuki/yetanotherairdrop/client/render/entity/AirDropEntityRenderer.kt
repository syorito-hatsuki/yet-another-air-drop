package dev.syoritohatsuki.yetanotherairdrop.client.render.entity

import dev.syoritohatsuki.yetanotherairdrop.entity.projectile.AirDropEntity
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.block.BlockRenderType
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.client.render.Frustum
import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.RenderLayers
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.BlockRenderManager
import net.minecraft.client.render.entity.EntityRenderer
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.render.entity.state.FallingBlockEntityRenderState
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.random.Random

@Environment(EnvType.CLIENT)
class AirDropEntityRenderer(context: EntityRendererFactory.Context) : EntityRenderer<AirDropEntity, FallingBlockEntityRenderState>(context) {

    private var blockRenderManager: BlockRenderManager = context.blockRenderManager

    init {
        shadowRadius = 0.5f
    }

    override fun shouldRender(entity: AirDropEntity, frustum: Frustum, d: Double, e: Double, f: Double): Boolean =
        when {
            !super.shouldRender(entity, frustum, d, e, f) -> false
            else -> Blocks.BARREL.defaultState != entity.world.getBlockState(entity.blockPos)
        }

    override fun render(
        fallingBlockEntityRenderState: FallingBlockEntityRenderState,
        matrixStack: MatrixStack,
        vertexConsumerProvider: VertexConsumerProvider,
        light: Int
    ) {
        val blockState: BlockState = fallingBlockEntityRenderState.blockState
        if (blockState.renderType == BlockRenderType.MODEL) {
            matrixStack.push()
            matrixStack.translate(-0.5, 0.0, -0.5)
            blockRenderManager
                .modelRenderer
                .render(
                    fallingBlockEntityRenderState,
                    blockRenderManager.getModel(blockState),
                    blockState,
                    fallingBlockEntityRenderState.currentPos,
                    matrixStack,
                    vertexConsumerProvider.getBuffer(RenderLayers.getMovingBlockLayer(blockState)),
                    false,
                    Random.create(),
                    blockState.getRenderingSeed(fallingBlockEntityRenderState.fallingBlockPos),
                    OverlayTexture.DEFAULT_UV
                )
            matrixStack.pop()
            super.render(fallingBlockEntityRenderState, matrixStack, vertexConsumerProvider, light)
        }

    }

    override fun createRenderState(): FallingBlockEntityRenderState = FallingBlockEntityRenderState()

    override fun updateRenderState(entity: AirDropEntity, state: FallingBlockEntityRenderState, tickDelta: Float) {
        super.updateRenderState(entity, state, tickDelta)
        val blockPos = BlockPos.ofFloored(
            entity.x,
            entity.boundingBox.maxY,
            entity.z
        )
        state.fallingBlockPos = entity.getFallingBlockPos()
        state.currentPos = blockPos
        state.blockState = Blocks.BARREL.defaultState
        state.biome = entity.world.getBiome(blockPos)
        state.world = entity.world
    }
}

