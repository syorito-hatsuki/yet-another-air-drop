package dev.syoritohatsuki.yetanotherairdrop.mixin;

import dev.syoritohatsuki.yetanotherairdrop.world.AirDropManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.spawner.SpecialSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.ArrayList;
import java.util.List;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @ModifyVariable(method = "createWorlds", at = @At("STORE"), ordinal = 0)
    private List<SpecialSpawner> injectAirDropManager(List<SpecialSpawner> list) {
        var _list = new ArrayList<>(List.copyOf(list));
        _list.add(new AirDropManager());
        return _list;
    }
}
