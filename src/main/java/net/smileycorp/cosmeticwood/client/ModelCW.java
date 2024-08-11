package net.smileycorp.cosmeticwood.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.IModelState;
import net.smileycorp.cosmeticwood.common.CWLogger;
import net.smileycorp.cosmeticwood.common.Constants;

import java.util.function.Function;

public class ModelCW implements IModel {
    
    private final ModelResourceLocation loc;
    
    public ModelCW(ModelResourceLocation loc) {
        this.loc = loc;
    }
    
    @Override
    public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
        ModelManager manager = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelManager();
        ModelResourceLocation loc2 = new ModelResourceLocation(Constants.loc(loc.getResourcePath()), loc.getVariant());
        try {
            return new BakedModelCW(manager.getModel(loc), ModelLoaderRegistry.getModel(loc2));
        } catch (Exception e) {
            CWLogger.logError("Failed loading model " + loc + "(" + loc2 + ")", e);
            return manager.getMissingModel();
        }
    }
    
}
