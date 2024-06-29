package dev.syoritohatsuki.yetanotherairdrop.entity

import dev.syoritohatsuki.yetanotherairdrop.YetAnotherAirDrop
import dev.syoritohatsuki.yetanotherairdrop.entity.projectile.AirDropEntity
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier


object EntityTypeRegistry {
    val AIR_DROP = register("air_drop", EntityType.Builder.create(::AirDropEntity, SpawnGroup.MISC)
        .dimensions(1f, 1f)
        .maxTrackingRange(32)
        .trackingTickInterval(2))

    private fun <T : Entity> register(id: String, type: EntityType.Builder<T>): EntityType<T> = Registry.register(
        Registries.ENTITY_TYPE, Identifier.of(YetAnotherAirDrop.MOD_ID, id), type.build(id)
    )
}