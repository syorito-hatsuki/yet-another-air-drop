package dev.syoritohatsuki.yetanotherairdrop.entity.projectile

import dev.syoritohatsuki.yetanotherairdrop.entity.EntityTypeRegistry
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.entity.ChestBlockEntity
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.MovementType
import net.minecraft.entity.data.DataTracker
import net.minecraft.loot.LootTables
import net.minecraft.nbt.NbtCompound
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class AirDropEntity(type: EntityType<AirDropEntity>, world: World) : Entity(type, world) {

    constructor(world: World, x: Double, y: Double, z: Double) : this(EntityTypeRegistry.AIR_DROP, world) {
        setPosition(x, y, z)
        yaw = (random.nextDouble() * 360.0).toFloat()
        setVelocity(
            (random.nextDouble() * 0.2f - 0.1f) * 2.0,
            random.nextDouble() * 0.2 * 2.0,
            (random.nextDouble() * 0.2f - 0.1f) * 2.0
        )
    }

    override fun onBlockCollision(state: BlockState) {
        super.onBlockCollision(state)

        if (!isOnGround) return
        if (trySetChest(state, blockPos)) return

        val newPos = blockPos.up()
        if (trySetChest(world.getBlockState(newPos), newPos)) return
    }

    private fun trySetChest(state: BlockState, blockPos: BlockPos): Boolean {
        if (!state.isReplaceable) return false

        if (world.setBlockState(blockPos, Blocks.CHEST.defaultState, Block.NOTIFY_ALL_AND_REDRAW)) {
            (world.getBlockEntity(blockPos) as ChestBlockEntity).lootTable = LootTables.RED_SHEEP_ENTITY
        }

        discard()

        server?.playerManager?.playerList?.forEach { entity ->
            entity.sendMessageToClient(Text.literal("Air Drop landed on [")
                .append(Text.literal("X: ${blockPos.x}").styled {
                    it.withBold(true).withColor(Formatting.RED)
                }).append(Text.literal(" | ")).append(Text.literal("Y: ${blockPos.y}").styled {
                    it.withBold(true).withColor(Formatting.GREEN)
                }).append(Text.literal(" | ")).append(Text.literal("Z: ${blockPos.z}").styled {
                    it.withBold(true).withColor(Formatting.BLUE)
                }).append(Text.literal("] in ")).append(
                    Text.translatable(world.dimensionEntry.key.get().value.path.replaceFirstChar { it.titlecase() })
                        .styled { it.withBold(true).withColor(Formatting.YELLOW) }), false
            )
        }

        return true
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

    override fun getGravity(): Double = 0.005

    override fun initDataTracker(builder: DataTracker.Builder) {
    }

    override fun readCustomDataFromNbt(nbt: NbtCompound) {
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound) {
    }
}