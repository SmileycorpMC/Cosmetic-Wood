package net.smileycorp.cosmeticwood.mixin;

import net.minecraft.client.renderer.GlStateManager;
import net.smileycorp.cosmeticwood.client.ClientProxy;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GlStateManager.class)
public class MixinGLStateManager {

    @Inject(at = @At("HEAD"), method = "color(FFFF)V", cancellable = true)
    private static void CW$color(float r, float g, float b, float a, CallbackInfo callback) {
        if (ClientProxy.GL_COLOUR_FROZEN) callback.cancel();
    }

    @Inject(at = @At("HEAD"), method = "popMatrix")
    private static void CW$popMatrix(CallbackInfo callback) {
        if (ClientProxy.GL_COLOUR_FROZEN) ClientProxy.unfreezeGLColour();
    }

}
