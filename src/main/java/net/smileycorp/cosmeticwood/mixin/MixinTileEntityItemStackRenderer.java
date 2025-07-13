package net.smileycorp.cosmeticwood.mixin;

import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.smileycorp.cosmeticwood.api.registry.item.WoodStack;
import net.smileycorp.cosmeticwood.client.ClientProxy;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TileEntityItemStackRenderer.class)
public class MixinTileEntityItemStackRenderer {

    @Inject(at = @At("HEAD"), method = "renderByItem(Lnet/minecraft/item/ItemStack;F)V")
    public void CW$renderByItem$HEAD(ItemStack stack, float partialTicks, CallbackInfo callback) {
        if (!((WoodStack)(Object)stack).isWood()) return;
        ResourceLocation type = ((WoodStack)(Object)stack).getType();
        if (type == null) return;
        if (!type.equals(((WoodStack)(Object)stack).getDefaultType())) ClientProxy.WOOD_TILE_TYPE = type;
    }

    @Inject(at = @At("TAIL"), method = "renderByItem(Lnet/minecraft/item/ItemStack;F)V")
    public void CW$renderByItem$TAIL(ItemStack stack, float partialTicks, CallbackInfo callback) {
        ClientProxy.resetTileData();
    }

}
