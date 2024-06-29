package dev.syoritohatsuki.yetanotherairdrop.entity.projectile

import dev.syoritohatsuki.yetanotherairdrop.entity.EntityTypeRegistry
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.MovementType
import net.minecraft.entity.data.DataTracker
import net.minecraft.nbt.NbtCompound
import net.minecraft.world.World

class AirDropEntity(type: EntityType<AirDropEntity>, world: World) : Entity(type, world) {

    constructor(world: World, x: Double, y: Double, z: Double) : this(EntityTypeRegistry.AIR_DROP, world) {
        this.setPosition(x, y, z)
        this.yaw = (random.nextDouble() * 360.0).toFloat()
        this.setVelocity(
            (random.nextDouble() * 0.2f - 0.1f) * 2.0,
            random.nextDouble() * 0.2 * 2.0,
            (random.nextDouble() * 0.2f - 0.1f) * 2.0
        )
    }

    override fun tick() {
        super.tick()
        prevX = x
        prevY = y
        prevZ = z
        applyGravity()

        move(MovementType.SELF, velocity)
        var f = 0.98f
        if (isOnGround) {
            f = world.getBlockState(velocityAffectingPos).block.slipperiness * 0.98f
        }

        velocity = velocity.multiply(f.toDouble(), 0.98, f.toDouble())
        if (isOnGround) {
            velocity = velocity.multiply(1.0, -0.9, 1.0)
        }
    }

    override fun getGravity(): Double = 0.0005

    override fun initDataTracker(builder: DataTracker.Builder) {
    }

    override fun readCustomDataFromNbt(nbt: NbtCompound) {
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound) {
    }
}