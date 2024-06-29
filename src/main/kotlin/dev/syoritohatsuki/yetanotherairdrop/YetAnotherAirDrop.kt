package dev.syoritohatsuki.yetanotherairdrop

import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import com.mojang.logging.LogUtils
import dev.syoritohatsuki.yetanotherairdrop.entity.EntityTypeRegistry
import dev.syoritohatsuki.yetanotherairdrop.entity.projectile.AirDropEntity
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.server.command.ServerCommandSource
import org.slf4j.Logger

object YetAnotherAirDrop : ModInitializer {

    const val MOD_ID = "yet-another-air-drop"
    val logger: Logger = LogUtils.getLogger()

    override fun onInitialize() {
        logger.info("${javaClass.simpleName} initialized with mod-id $MOD_ID")

        EntityTypeRegistry

        CommandRegistrationCallback.EVENT.register { dispatcher, _, _ ->
            dispatcher.register(
                literal<ServerCommandSource>("air-drop").then(
                    literal<ServerCommandSource>("spawn").executes {
                        val cord = it.source.player?.pos!!
                        it.source.player?.serverWorld?.spawnEntity(AirDropEntity(it.source.world, cord.x, cord.y, cord.z))
                        1
                    }
                )
            )
        }
    }
}