package dev.syoritohatsuki.yetanotherairdrop

import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import com.mojang.logging.LogUtils
import dev.syoritohatsuki.yetanotherairdrop.entity.EntityTypeRegistry
import dev.syoritohatsuki.yetanotherairdrop.entity.projectile.AirDropEntity
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text
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
            dispatcher.register(literal<ServerCommandSource>(MOD_ID).then(literal<ServerCommandSource>("spawn").executes {
                it.source.player?.pos?.apply {
                    val drop = DatapackLoader.getRandomDrop(it.source.world.registryKey.value) ?: run {
                        it.source.sendFeedback({ Text.literal("Can't get drop for your world") }, false)
                        return@executes 0
                    }
                    it.source.player?.serverWorld?.spawnEntity(AirDropEntity(it.source.world, x, y, z, drop))
                }
                1
            }))
        }
    }
}