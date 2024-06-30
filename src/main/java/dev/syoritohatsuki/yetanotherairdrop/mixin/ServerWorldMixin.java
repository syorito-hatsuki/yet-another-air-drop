package dev.syoritohatsuki.yetanotherairdrop.mixin;

import dev.syoritohatsuki.yetanotherairdrop.world.AirDropManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.level.ServerWorldProperties;
import net.minecraft.world.spawner.SpecialSpawner;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(ServerWorld.class)
public class ServerWorldMixin {
    @Mutable
    @Shadow
    @Final private List<SpecialSpawner> spawners;

    @Shadow @Final private ServerWorldProperties worldProperties;

    @Inject(method = "<init>", at = @At(value = "TAIL"))
    private void injectAirDropManager(CallbackInfo ci) {
        var list = new ArrayList<>(List.copyOf(spawners));
        list.add(new AirDropManager(worldProperties));
        spawners = list;
    }
}
