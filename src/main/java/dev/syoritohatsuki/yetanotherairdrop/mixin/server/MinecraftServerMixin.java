package dev.syoritohatsuki.yetanotherairdrop.mixin.server;

import dev.syoritohatsuki.yetanotherairdrop.server.AirDropManagerAccessor;
import dev.syoritohatsuki.yetanotherairdrop.world.AirDropManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.spawner.SpecialSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.ArrayList;
import java.util.List;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin implements AirDropManagerAccessor {

    @Unique
    public AirDropManager airDropManager;

    @ModifyVariable(method = "createWorlds", at = @At("STORE"), ordinal = 0)
    private List<SpecialSpawner> injectAirDropManager(List<SpecialSpawner> list) {
        var _list = new ArrayList<>(List.copyOf(list));
        var airDropManager = new AirDropManager((MinecraftServer) (Object) this);
        _list.add(airDropManager);
        setYet_another_air_drop$getAirDropManager(airDropManager);
        return _list;
    }

    @Unique
    public AirDropManager getYet_another_air_drop$getAirDropManager() {
        return airDropManager;
    }

    @Unique
    public void setYet_another_air_drop$getAirDropManager(AirDropManager airDropManager) {
        this.airDropManager = airDropManager;
    }
}
