package net.smileycorp.cosmeticwood.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.smileycorp.cosmeticwood.api.registry.item.WoodStack;
import net.smileycorp.cosmeticwood.client.TileContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TileEntityItemStackRenderer.class)
public abstract class MixinTileEntityItemStackRenderer {

    @Shadow public abstract void renderByItem(ItemStack p_192838_1_, float partialTicks);

    @Inject(at = @At("HEAD"), method = "renderByItem(Lnet/minecraft/item/ItemStack;F)V")
    public void CW$renderByItem$HEAD(ItemStack stack, float partialTicks, CallbackInfo callback) {
        if (!((WoodStack)(Object)stack).isWood()) return;
        if (TileContext.isOverlay()) return;
        ResourceLocation type = ((WoodStack)(Object)stack).getType();
        if (type == null) return;
        if (!type.equals(((WoodStack)(Object)stack).getDefaultType())) TileContext.setWoodTileType(type);
    }

    @Inject(at = @At("TAIL"), method = "renderByItem(Lnet/minecraft/item/ItemStack;F)V")
    public void CW$renderByItem$TAIL(ItemStack stack, float partialTicks, CallbackInfo callback) {
        if (!((WoodStack)(Object)stack).isWood()) return;
        if (TileContext.isOverlay()) return;
        TileContext.unfreezeGLColour();
        ResourceLocation texture = TileContext.getOverlayTexture();
        if (texture != null) {
            TileContext.markOverlay();
            Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
            renderByItem(stack, partialTicks);
        }
        TileContext.resetTileData();
    }

}
