package net.smileycorp.cosmeticwood.mixin;

import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.util.datafix.DataFixer;
import net.smileycorp.cosmeticwood.common.registry.ContentRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.net.Proxy;

@Mixin(MinecraftServer.class)
public class MixinMinecraftServer {
    
    @Inject(at = @At("TAIL"), method = "<init>")
    public void CW$init(File world, Proxy proxy, DataFixer fixer, YggdrasilAuthenticationService authService, MinecraftSessionService sessionService, GameProfileRepository profileRepository, PlayerProfileCache profileCache, CallbackInfo callback) {
        ContentRegistry.generateData();
    }
    
}
