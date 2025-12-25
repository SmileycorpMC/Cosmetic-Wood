package net.smileycorp.cosmeticwood.mixin;

import net.minecraft.client.gui.FontRenderer;
import net.smileycorp.cosmeticwood.client.TileContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FontRenderer.class)
public class MixinFontRenderer {

    @Inject(at = @At("HEAD"), method = "setColor", remap = false)
    public void cw$setColor$HEAD(float r, float g, float b, float a, CallbackInfo callback) {
        TileContext.setBypassFreeze(true);
    }

    @Inject(at = @At("TAIL"), method = "setColor", remap = false)
    public void cw$setColor$TAIL(float r, float g, float b, float a, CallbackInfo callback) {
        TileContext.setBypassFreeze(false);
    }

}
