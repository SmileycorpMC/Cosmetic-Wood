package net.smileycorp.cosmeticwood.mixin;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelBlock;
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
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

@Mixin(value = ModelBakery.class, remap = false)
public abstract class MixinModelBakery {
    
    @Shadow protected abstract void loadBlock(BlockStateMapper blockstatemapper, Block block, ResourceLocation resourcelocation);
    
    @Shadow(remap = true) protected abstract ModelBlockDefinition getModelBlockDefinition(ResourceLocation location);
    
    @Shadow(remap = true) @Final private Map<ResourceLocation, ModelBlockDefinition> blockDefinitions;
    
    @Shadow @Final private Map<ResourceLocation, ModelBlock> models;
    
    @Shadow protected abstract ModelBlock loadModel(ResourceLocation location) throws IOException;
    
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
            //this.models.put(model, loadModel(model));
            CWLogger.logInfo("Loaded wood model state " + model);
        }
    }
    //@Inject(at = @At("HEAD"), method = "getModelBlockDefinition", remap = true)
    public void oingoSploingo(ResourceLocation loc, CallbackInfoReturnable<ModelBlockDefinition> cir) {
        if (!loc.getResourceDomain().equals(Constants.MODID)) return;
        CWLogger.logInfo("grinkl");
        CWLogger.logInfo("[oingo sploingo] " + blockDefinitions.get(loc));
    }
    
    @Inject(at = @At("TAIL"), method = "loadMultipartMBD", remap = true)
    public void oingoSploingo2(ResourceLocation loc1, ResourceLocation loc2, CallbackInfoReturnable<ModelBlockDefinition> cir) {
        if (!loc1.getResourceDomain().equals(Constants.MODID)) return;
        CWLogger.logInfo("gronkl");
        CWLogger.logInfo("[oingo sploingo2] " + loc1 + ", " + loc2);
        ModelBlockDefinition mbd = cir.getReturnValue();
        CWLogger.logInfo(mbd);
        CWLogger.logInfo(mbd.getMultipartVariants());
    }
    
}
