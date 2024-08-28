package net.smileycorp.cosmeticwood.mixin;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.BlockStateMapper;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.smileycorp.cosmeticwood.client.CWModelLoader;
import net.smileycorp.cosmeticwood.common.registry.block.WoodBlock;
import net.smileycorp.cosmeticwood.common.registry.item.WoodItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ModelBakery.class, remap = false)
public class MixinModelBakery {
    
    private static boolean wood;
    
    @Inject(at = @At(value = "HEAD"), method = "registerItemVariants")
    private static void CW$registerItemVariants(Item item, ResourceLocation[] locs, CallbackInfo callback) {
        if (((WoodItem)item).isWood()) CWModelLoader.registerModels(locs);
    }
    
    @Inject(at = @At(value = "HEAD"), method = "loadBlock")
    private void CW$loadBlock$HEAD(BlockStateMapper mapper, Block block, ResourceLocation state, CallbackInfo callback) {
        if (!((WoodBlock)block).isWood()) return;
        CWModelLoader.registerModels(mapper.getVariants(block).values());
        CWModelLoader.registerModels(new ModelResourceLocation(state, "inventory"));
    }
    
}
