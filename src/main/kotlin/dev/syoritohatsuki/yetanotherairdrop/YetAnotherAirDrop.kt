package dev.syoritohatsuki.yetanotherairdrop

import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import com.mojang.logging.LogUtils
import dev.syoritohatsuki.yetanotherairdrop.entity.EntityTypeRegistry
import dev.syoritohatsuki.yetanotherairdrop.entity.projectile.AirDropEntity
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.minecraft.server.command.ServerCommandSource
import org.slf4j.Logger

object YetAnotherAirDrop : ModInitializer {

    const val MOD_ID = "yet-another-air-drop"
    val logger: Logger = LogUtils.getLogger()

    override fun onInitialize() {
        logger.info("${javaClass.simpleName} initialized with mod-id $MOD_ID")

        ServerLifecycleEvents.SERVER_STARTED.register {
            DatapackLoader.register(it)
            it.commandManager.executeWithPrefix(it.commandSource, "reload")
        }

        EntityTypeRegistry

        CommandRegistrationCallback.EVENT.register { dispatcher, _, _ ->
            dispatcher.register(literal<ServerCommandSource>(MOD_ID).requires {
                it.hasPermissionLevel(4)
            }.then(literal<ServerCommandSource>("spawn").executes {
                it.source.player?.pos?.let { vector ->
                    it.source.player?.serverWorld?.spawnEntity(
                        AirDropEntity(it.source.world, vector.x, vector.y, vector.z)
                    )
                    return@executes 1
                }
                throw RuntimeException("Can't spawn entity. Player is in superposition")
            }))
        }
    }
}
