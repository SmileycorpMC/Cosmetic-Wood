package net.smileycorp.cosmeticwood.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.smileycorp.cosmeticwood.client.ClientProxy;
import net.smileycorp.cosmeticwood.common.Constants;
import net.smileycorp.cosmeticwood.common.data.WoodHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(TileEntitySpecialRenderer.class)
public class MixinTileEntitySpecialRenderer {

    @Inject(at = @At("HEAD"), method = "bindTexture", cancellable = true)
    public void CW$bindTexture(ResourceLocation loc, CallbackInfo callback) {
        if (ClientProxy.WOOD_TILE_TYPE == null)  {
            ClientProxy.unfreezeGLColour();
            return;
        }
        Minecraft mc = Minecraft.getMinecraft();
        IResourceManager rm = mc.getResourceManager();
        TextureManager tm = mc.getTextureManager();
        ResourceLocation type = ClientProxy.WOOD_TILE_TYPE;
        ResourceLocation main = Constants.loc(loc.getResourcePath()
                .replace("textures/", "textures/" + loc.getResourceDomain() + "/"));
        try {
            ResourceLocation sub = Constants.loc(main.getResourcePath().replace(".png", "_"
                            + type.toString().replace(":", "_") + ".png"));
            rm.getResource(sub);
            tm.bindTexture(sub);
            callback.cancel();
        } catch (Exception e) {
            try {
                rm.getResource(main);
                tm.bindTexture(main);
                Color colour = WoodHandler.getInstance().getColour(type);
                GlStateManager.color((float) colour.getRed() / 255f, (float) colour.getGreen() / 255f,
                        (float) colour.getBlue() / 255f, (float) colour.getAlpha() / 255f);
                ClientProxy.GL_COLOUR_FROZEN = true;
                callback.cancel();
            } catch (Exception f) {}
        }
    }

}
