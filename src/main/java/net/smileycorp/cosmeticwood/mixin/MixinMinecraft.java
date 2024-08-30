package net.smileycorp.cosmeticwood.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.FolderResourcePack;
import net.minecraft.client.resources.IResourcePack;
import net.smileycorp.cosmeticwood.common.CWLogger;
import net.smileycorp.cosmeticwood.common.registry.ContentRegistry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Minecraft.class)
public class MixinMinecraft {
    
    @Shadow @Final private List<IResourcePack> defaultResourcePacks;
    
    @Inject(at = @At("HEAD"), method = "init", cancellable = true)
    public void CW$getResourcePackFiles(CallbackInfo callback) {
        try {
            ContentRegistry.generateData();
            defaultResourcePacks.add(new FolderResourcePack(ContentRegistry.CONFIG_FOLDER.toFile()));
        } catch (Exception e) {
            CWLogger.logError("Failed loading config resources", e);
        }
    }
    
}
