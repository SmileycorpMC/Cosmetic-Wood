package net.smileycorp.cosmeticwood.mixin;

import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.smileycorp.cosmeticwood.api.registry.item.WoodStack;
import net.smileycorp.cosmeticwood.client.ClientProxy;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TileEntityRendererDispatcher.class)
public class MixinTileEntityRendererDispatcher {

    @Inject(at = @At("HEAD"), method = "render(Lnet/minecraft/tileentity/TileEntity;DDDFIF)V")
    public void CW$render$HEAD(TileEntity tile, double x, double y, double z, float partialTicks, int destroyStage, float scale, CallbackInfo callback) {
        if (!((WoodStack)tile).isWood()) return;
        ResourceLocation type = ((WoodStack)tile).getType();
        if (type == null) return;
        if (!type.equals(((WoodStack)tile).getDefaultType())) ClientProxy.WOOD_TILE_TYPE = type;
    }

    @Inject(at = @At("TAIL"), method = "render(Lnet/minecraft/tileentity/TileEntity;DDDFIF)V")
    public void CW$render$TAIL(TileEntity tile, double x, double y, double z, float partialTicks, int destroyStage, float scale, CallbackInfo callback) {
        ClientProxy.resetTileData();
    }

}
