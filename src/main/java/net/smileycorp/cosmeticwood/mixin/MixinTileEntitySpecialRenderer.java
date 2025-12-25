package net.smileycorp.cosmeticwood.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.smileycorp.cosmeticwood.client.TileContext;
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
        if (TextureMap.LOCATION_BLOCKS_TEXTURE.equals(loc)) return;
        if (TileContext.isOverlay()) {
            callback.cancel();
            return;
        }
        if (TileContext.getWoodTileType() == null)  {
            TileContext.unfreezeGLColour();
            return;
        }
        Minecraft mc = Minecraft.getMinecraft();
        IResourceManager rm = mc.getResourceManager();
        TextureManager tm = mc.getTextureManager();
        ResourceLocation type = TileContext.getWoodTileType();
        ResourceLocation main = Constants.loc(loc.getResourcePath()
                .replace("textures/", "textures/" + loc.getResourceDomain() + "/"));
        try {
            ResourceLocation sub = Constants.loc(main.getResourcePath().replace(".png", "_"
                            + type.toString().replace(":", "_") + ".png"));
            rm.getResource(sub);
            tm.bindTexture(sub);
            callback.cancel();
            TileContext.setSubOverlayTexture(Constants.loc(main.getResourcePath()
                    .replace(".png", "_overlay.png")));
        } catch (Exception e) {
            try {
                rm.getResource(main);
                tm.bindTexture(main);
                Color colour = WoodHandler.getInstance().getColour(type);
                GlStateManager.color((float) colour.getRed() / 255f, (float) colour.getGreen() / 255f,
                        (float) colour.getBlue() / 255f, (float) colour.getAlpha() / 255f);
                TileContext.freezeGLColour();
                callback.cancel();
            } catch (Exception f) {
                return;
            }
        }
        TileContext.setOverlayTexture(Constants.loc(main.getResourcePath().replace(".png", "_overlay.png")));
    }

}
