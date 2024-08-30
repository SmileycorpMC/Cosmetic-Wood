package net.smileycorp.cosmeticwood.mixin;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelBlockDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.BlockStateMapper;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.smileycorp.cosmeticwood.api.registry.block.WoodBlock;
import net.smileycorp.cosmeticwood.api.registry.item.WoodItem;
import net.smileycorp.cosmeticwood.client.CWModelLoader;
import net.smileycorp.cosmeticwood.common.CWLogger;
import net.smileycorp.cosmeticwood.common.Constants;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;

@Mixin(value = ModelBakery.class, remap = false)
public abstract class MixinModelBakery {
    
    @Shadow protected abstract void loadBlock(BlockStateMapper blockstatemapper, Block block, ResourceLocation resourcelocation);
    
    @Shadow(remap = true) protected abstract ModelBlockDefinition getModelBlockDefinition(ResourceLocation location);
    
    @Inject(at = @At(value = "HEAD"), method = "registerItemVariants")
    private static void CW$registerItemVariants(Item item, ResourceLocation[] locs, CallbackInfo callback) {
        if (((WoodItem)item).isWood()) CWModelLoader.registerModels(locs);
    }
    
    @Inject(at = @At(value = "HEAD"), method = "loadBlock")
    private void CW$loadBlock$HEAD(BlockStateMapper mapper, Block block, ResourceLocation state, CallbackInfo callback) {
        if (!((WoodBlock)block).isWood()) return;
        if (state.getResourceDomain().equals(Constants.MODID)) return;
        Collection<? extends ResourceLocation> models = CWModelLoader.registerModels(mapper.getVariants(block).values());
        CWModelLoader.registerModels(new ModelResourceLocation(state, "inventory"));
        for (ResourceLocation model : models) {
            loadBlock(mapper, block, model);
            CWLogger.logInfo("Loaded wood model state " + model);
        }
    }
    
    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/block/model/ModelBakery;getModelBlockDefinition(Lnet/minecraft/util/ResourceLocation;)Lnet/minecraft/client/renderer/block/model/ModelBlockDefinition;", remap = true), method = "loadBlock")
    public ModelBlockDefinition CW$loadBlock$getModelBlockDefinition(ModelBakery instance, ResourceLocation loc) {
        if (loc.getResourceDomain().equals(Constants.MODID) && loc instanceof ModelResourceLocation) {
            String[] split = loc.getResourcePath().split("/");
            ModelResourceLocation loc1 = new ModelResourceLocation(new ResourceLocation(split[0], split[1]), ((ModelResourceLocation) loc).getVariant());
            CWLogger.logInfo("remapping " + loc + " to " + loc1);
        }
        return getModelBlockDefinition(loc);
    }
    
    @Inject(at = @At("TAIL"), method = "registerMultipartVariant")
    public void oingoSploingo(ModelBlockDefinition p_registerMultipartVariant_1_, Collection<ModelResourceLocation> p_registerMultipartVariant_2_, CallbackInfo ci) {
        CWLogger.logInfo("gronkl");
        CWLogger.logInfo("[oingo sploingo] " + p_registerMultipartVariant_1_ + ", " + p_registerMultipartVariant_2_);
    }
    
}
