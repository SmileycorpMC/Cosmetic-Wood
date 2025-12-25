package net.smileycorp.cosmeticwood.mixin;

import net.minecraft.client.renderer.tileentity.TileEntityChestRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//test class to use for testing chest rendering during winter
//unused in release version, here in case needed for testing again
@Mixin(TileEntityChestRenderer.class)
public class MixinTESRChest {

    @Shadow private boolean isChristmas;

    @Inject(at= @At("TAIL"), method = "<init>")
    public void init(CallbackInfo callback) {
        isChristmas = false;
    }

}
