package dev.syoritohatsuki.yetanotherairdrop.client.render.entity.model

import net.minecraft.client.model.*
import net.minecraft.client.render.entity.model.EntityModel
import net.minecraft.client.render.entity.state.EntityRenderState

class AirDropEntityModel(root: ModelPart) : EntityModel<EntityRenderState?>(root) {
    companion object {
        fun getTexturedModelData(): TexturedModelData = TexturedModelData.of(ModelData().apply {
            root.addChild(
                "root",
                ModelPartBuilder.create().uv(0, 0).cuboid(-8.0f, 0.0f, -8.0f, 16.0f, 16.0f, 16.0f, Dilation(0.0f)),
                ModelTransform.pivot(0.0f, 0.0f, 0.0f)
            ).let { root ->
                root.addChild(
                    "balloons", ModelPartBuilder.create(), ModelTransform.pivot(0.0f, 16.0f, 0.0f)
                ).let { balloons ->
                    balloons.addChild(
                        "balloon_1",
                        ModelPartBuilder.create().uv(0, 77)
                            .cuboid(-3.0f, 3.0f, -3.0f, 6.0f, 10.0f, 6.0f, Dilation(0.0f)).uv(88, 43)
                            .cuboid(-1.0f, 2.0f, -1.0f, 2.0f, 1.0f, 2.0f, Dilation(0.0f)).uv(64, 0)
                            .cuboid(-4.0f, 5.0f, -4.0f, 8.0f, 7.0f, 8.0f, Dilation(0.0f)).uv(88, 58)
                            .cuboid(-0.5f, 0.0f, -0.5f, 1.0f, 2.0f, 1.0f, Dilation(0.0f)),
                        ModelTransform.pivot(1.0f, 0.0f, -1.0f)
                    )

                    balloons.addChild(
                        "balloon_2",
                        ModelPartBuilder.create().uv(0, 77)
                            .cuboid(-3.0f, 8.0f, -3.0f, 6.0f, 10.0f, 6.0f, Dilation(0.0f)).uv(88, 43)
                            .cuboid(-1.0f, 7.0f, -1.0f, 2.0f, 1.0f, 2.0f, Dilation(0.0f)).uv(64, 0)
                            .cuboid(-4.0f, 10.0f, -4.0f, 8.0f, 7.0f, 8.0f, Dilation(0.0f)).uv(88, 29)
                            .cuboid(-0.5f, 0.0f, -0.5f, 1.0f, 7.0f, 1.0f, Dilation(0.0f)),
                        ModelTransform.pivot(-7.5f, 0.0f, -2.5f)
                    )

                    balloons.addChild(
                        "balloon_3",
                        ModelPartBuilder.create().uv(0, 77).cuboid(
                            -3.0f, 14.375f, -3.0f, 6.0f, 10.0f, 6.0f, Dilation(0.0f)
                        ).uv(88, 43).cuboid(-1.0f, 13.375f, -1.0f, 2.0f, 1.0f, 2.0f, Dilation(0.0f)).uv(64, 0)
                            .cuboid(-4.0f, 16.375f, -4.0f, 8.0f, 7.0f, 8.0f, Dilation(0.0f)).uv(88, 15)
                            .cuboid(-0.5f, 0.375f, -0.5f, 1.0f, 13.0f, 1.0f, Dilation(0.0f)),
                        ModelTransform.pivot(-6.0f, -0.375f, 6.0f)
                    )

                    balloons.addChild(
                        "balloon_4",
                        ModelPartBuilder.create().uv(0, 77).cuboid(
                            -3.0f, 36.0f, -3.0f, 6.0f, 10.0f, 6.0f, Dilation(0.0f)
                        ).uv(88, 43).cuboid(-1.0f, 35.0f, -1.0f, 2.0f, 1.0f, 2.0f, Dilation(0.0f)).uv(64, 0)
                            .cuboid(-4.0f, 38.0f, -4.0f, 8.0f, 7.0f, 8.0f, Dilation(0.0f)).uv(72, 79)
                            .cuboid(-0.5f, 0.0f, -0.5f, 1.0f, 35.0f, 1.0f, Dilation(0.0f)),
                        ModelTransform.pivot(-1.5f, 0.0f, 3.5f)
                    )

                    balloons.addChild(
                        "balloon_5",
                        ModelPartBuilder.create().uv(0, 77).cuboid(
                            -3.0f, 25.0f, -3.0f, 6.0f, 10.0f, 6.0f, Dilation(0.0f)
                        ).uv(88, 43).cuboid(-1.0f, 24.0f, -1.0f, 2.0f, 1.0f, 2.0f, Dilation(0.0f)).uv(64, 0)
                            .cuboid(-4.0f, 27.0f, -4.0f, 8.0f, 7.0f, 8.0f, Dilation(0.0f)).uv(76, 79)
                            .cuboid(-0.5f, 0.0f, -0.5f, 1.0f, 24.0f, 1.0f, Dilation(0.0f)),
                        ModelTransform.pivot(4.0f, 0.0f, 5.0f)
                    )

                    balloons.addChild(
                        "balloon_6",
                        ModelPartBuilder.create().uv(0, 77).cuboid(
                            -3.0f, 15.0f, -3.0f, 6.0f, 10.0f, 6.0f, Dilation(0.0f)
                        ).uv(88, 43).cuboid(-1.0f, 14.0f, -1.0f, 2.0f, 1.0f, 2.0f, Dilation(0.0f)).uv(64, 0)
                            .cuboid(-4.0f, 17.0f, -4.0f, 8.0f, 7.0f, 8.0f, Dilation(0.0f)).uv(84, 79)
                            .cuboid(-0.5f, 0.0f, -0.5f, 1.0f, 14.0f, 1.0f, Dilation(0.0f)),
                        ModelTransform.pivot(7.0f, 0.0f, -3.0f)
                    )

                    balloons.addChild(
                        "balloon_7",
                        ModelPartBuilder.create().uv(0, 77).cuboid(
                            -3.0f, 17.0f, -3.0f, 6.0f, 10.0f, 6.0f, Dilation(0.0f)
                        ).uv(88, 43).cuboid(-1.0f, 16.0f, -1.0f, 2.0f, 1.0f, 2.0f, Dilation(0.0f)).uv(64, 0)
                            .cuboid(-4.0f, 19.0f, -4.0f, 8.0f, 7.0f, 8.0f, Dilation(0.0f)).uv(80, 79)
                            .cuboid(-0.5f, 0.0f, -0.5f, 1.0f, 16.0f, 1.0f, Dilation(0.0f)),
                        ModelTransform.pivot(-1.0f, 0.0f, -7.0f)
                    )
                }
            }
        }, 128, 128)
    }
}