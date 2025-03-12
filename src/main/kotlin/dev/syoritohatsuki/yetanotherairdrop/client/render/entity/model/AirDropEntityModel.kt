// Made with Blockbench 4.12.3
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports

package dev.syoritohatsuki.yetanotherairdrop.client.render.entity.model;

import net.minecraft.client.model.*
import net.minecraft.client.render.entity.model.EntityModel
import net.minecraft.client.render.entity.state.EntityRenderState

class AirDropEntityModel(root: ModelPart) : EntityModel<EntityRenderState>(root) {
    companion object {
        fun getTexturedModelData(): TexturedModelData {
            val modelData = ModelData()
            val modelPartData = modelData.root

            val root = modelPartData.addChild(
                "root",
                ModelPartBuilder.create().uv(0, 0).cuboid(-8.0F, 0.0F, -8.0F, 16.0F, 16.0F, 16.0F, Dilation(0.0F)),
                ModelTransform.pivot(0.0F, 0.0F, 0.0F)
            )

            val balls = root.addChild(
                "balls",
                ModelPartBuilder.create().uv(0, 33).cuboid(-11.5F, 16.0F, 2.5F, 1.0F, 2.0F, 1.0F, Dilation(0.0F)),
                ModelTransform.pivot(7.5F, 0.0F, -8.5F)
            )

            balls.addChild(
                "ball",
                ModelPartBuilder.create().uv(38, 33).cuboid(-14.0F, 19.0F, 0.0F, 6.0F, 10.0F, 6.0F, Dilation(0.0F))
                    .uv(5, 33).cuboid(-15.0F, 21.0F, -1.0F, 8.0F, 7.0F, 8.0F, Dilation(0.0F)).uv(5, 49)
                    .cuboid(-12.0F, 18.0F, 2.0F, 2.0F, 1.0F, 2.0F, Dilation(0.0F)),
                ModelTransform.pivot(0.0F, 0.0F, 0.0F)
            )

            val balls2 = root.addChild(
                "balls2",
                ModelPartBuilder.create().uv(0, 33).cuboid(-11.5F, 1.0F, 2.5F, 1.0F, 17.0F, 1.0F, Dilation(0.0F)),
                ModelTransform.pivot(12.5F, 15.0F, -10.0F)
            )

            balls2.addChild(
                "ball2",
                ModelPartBuilder.create().uv(5, 49).cuboid(-7.0F, 33.0F, 0.5F, 2.0F, 1.0F, 2.0F, Dilation(0.0F))
                    .uv(5, 33).cuboid(-10.0F, 36.0F, -2.5F, 8.0F, 7.0F, 8.0F, Dilation(0.0F)).uv(38, 33)
                    .cuboid(-9.0F, 34.0F, -1.5F, 6.0F, 10.0F, 6.0F, Dilation(0.0F)),
                ModelTransform.pivot(-5.0F, -15.0F, 1.5F)
            )

            val balls3 = root.addChild(
                "balls3",
                ModelPartBuilder.create().uv(0, 33).cuboid(-11.5F, 12.0F, 2.5F, 1.0F, 6.0F, 1.0F, Dilation(0.0F)),
                ModelTransform.pivot(18.5F, 4.0F, -8.5F)
            )

            balls3.addChild(
                "ball3",
                ModelPartBuilder.create().uv(38, 33).cuboid(-3.0F, 23.0F, 0.0F, 6.0F, 10.0F, 6.0F, Dilation(0.0F))
                    .uv(5, 33).cuboid(-4.0F, 25.0F, -1.0F, 8.0F, 7.0F, 8.0F, Dilation(0.0F)).uv(5, 49)
                    .cuboid(-1.0F, 22.0F, 2.0F, 2.0F, 1.0F, 2.0F, Dilation(0.0F)),
                ModelTransform.pivot(-11.0F, -4.0F, 0.0F)
            )

            val balls4 = root.addChild(
                "balls4",
                ModelPartBuilder.create().uv(0, 33).cuboid(-11.5F, -12.0F, 2.5F, 1.0F, 30.0F, 1.0F, Dilation(0.0F)),
                ModelTransform.pivot(12.0F, 28.0F, -5.0F)
            )

            balls4.addChild(
                "ball4",
                ModelPartBuilder.create().uv(38, 33).cuboid(-9.5F, 47.0F, 3.5F, 6.0F, 10.0F, 6.0F, Dilation(0.0F))
                    .uv(5, 33).cuboid(-10.5F, 49.0F, 2.5F, 8.0F, 7.0F, 8.0F, Dilation(0.0F)).uv(5, 49)
                    .cuboid(-7.5F, 46.0F, 5.5F, 2.0F, 1.0F, 2.0F, Dilation(0.0F)),
                ModelTransform.pivot(-4.5F, -28.0F, -3.5F)
            )

            val balls5 = root.addChild(
                "balls5",
                ModelPartBuilder.create().uv(0, 33).cuboid(-11.5F, 3.0F, 2.5F, 1.0F, 15.0F, 1.0F, Dilation(0.0F)),
                ModelTransform.pivot(18.0F, 13.0F, -1.0F)
            )

            balls5.addChild(
                "ball5",
                ModelPartBuilder.create().uv(38, 33).cuboid(-3.5F, 32.0F, 7.5F, 6.0F, 10.0F, 6.0F, Dilation(0.0F))
                    .uv(5, 33).cuboid(-4.5F, 34.0F, 6.5F, 8.0F, 7.0F, 8.0F, Dilation(0.0F)).uv(5, 49)
                    .cuboid(-1.5F, 31.0F, 9.5F, 2.0F, 1.0F, 2.0F, Dilation(0.0F)),
                ModelTransform.pivot(-10.5F, -13.0F, -7.5F)
            )

            val balls6 = root.addChild(
                "balls6",
                ModelPartBuilder.create().uv(0, 33).cuboid(-11.5F, 15.0F, 2.5F, 1.0F, 3.0F, 1.0F, Dilation(0.0F)),
                ModelTransform.pivot(15.5F, 1.0F, 4.5F)
            )

            balls6.addChild(
                "ball6",
                ModelPartBuilder.create().uv(38, 33).cuboid(-6.0F, 20.0F, 13.0F, 6.0F, 10.0F, 6.0F, Dilation(0.0F))
                    .uv(5, 33).cuboid(-7.0F, 22.0F, 12.0F, 8.0F, 7.0F, 8.0F, Dilation(0.0F)).uv(5, 49)
                    .cuboid(-4.0F, 19.0F, 15.0F, 2.0F, 1.0F, 2.0F, Dilation(0.0F)),
                ModelTransform.pivot(-8.0F, -1.0F, -13.0F)
            )

            val balls7 = root.addChild(
                "balls7",
                ModelPartBuilder.create().uv(0, 33).cuboid(-11.5F, -5.0F, 2.5F, 1.0F, 23.0F, 1.0F, Dilation(0.0F)),
                ModelTransform.pivot(10.0F, 21.0F, 2.25F)
            )

            balls7.addChild(
                "ball7",
                ModelPartBuilder.create().uv(38, 33).cuboid(-11.5F, 40.0F, 10.75F, 6.0F, 10.0F, 6.0F, Dilation(0.0F))
                    .uv(5, 33).cuboid(-12.5F, 42.0F, 9.75F, 8.0F, 7.0F, 8.0F, Dilation(0.0F)).uv(5, 49)
                    .cuboid(-9.5F, 39.0F, 12.75F, 2.0F, 1.0F, 2.0F, Dilation(0.0F)),
                ModelTransform.pivot(-2.5F, -21.0F, -10.75F)
            )

            val balls8 = root.addChild(
                "balls8",
                ModelPartBuilder.create().uv(0, 33).cuboid(-11.5F, 14.0F, 2.5F, 1.0F, 4.0F, 1.0F, Dilation(0.0F)),
                ModelTransform.pivot(5.0F, 2.0F, 3.5F)
            )

            balls8.addChild(
                "ball8",
                ModelPartBuilder.create().uv(38, 33).cuboid(-16.5F, 21.0F, 12.0F, 6.0F, 10.0F, 6.0F, Dilation(0.0F))
                    .uv(5, 33).cuboid(-17.5F, 23.0F, 11.0F, 8.0F, 7.0F, 8.0F, Dilation(0.0F)).uv(5, 49)
                    .cuboid(-14.5F, 20.0F, 14.0F, 2.0F, 1.0F, 2.0F, Dilation(0.0F)),
                ModelTransform.pivot(2.5F, -2.0F, -12.0F)
            )

            val balls9 = root.addChild(
                "balls9",
                ModelPartBuilder.create().uv(0, 33).cuboid(-11.5F, 4.0F, 2.5F, 1.0F, 14.0F, 1.0F, Dilation(0.0F)),
                ModelTransform.pivot(3.5F, 12.0F, -3.5F)
            )

			balls9.addChild(
                "ball9",
                ModelPartBuilder.create().uv(38, 33).cuboid(-18.0F, 31.0F, 5.0F, 6.0F, 10.0F, 6.0F, Dilation(0.0F))
                    .uv(5, 33).cuboid(-19.0F, 33.0F, 4.0F, 8.0F, 7.0F, 8.0F, Dilation(0.0F)).uv(5, 49)
                    .cuboid(-16.0F, 30.0F, 7.0F, 2.0F, 1.0F, 2.0F, Dilation(0.0F)),
                ModelTransform.pivot(4.0F, -12.0F, -5.0F)
            )

            return TexturedModelData.of(modelData, 64, 64);
        }
    }
}
