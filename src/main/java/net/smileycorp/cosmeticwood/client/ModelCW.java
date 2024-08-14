package net.smileycorp.cosmeticwood.client;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.IModelState;
import net.smileycorp.cosmeticwood.common.CWLogger;
import net.smileycorp.cosmeticwood.common.Constants;

import java.util.Collection;
import java.util.function.Function;

public class ModelCW implements IModel {
    
    private final ResourceLocation original, base;
    
    public ModelCW(ResourceLocation loc) {
        this.original = loc;
        base = loc instanceof ModelResourceLocation ? new ModelResourceLocation(Constants.loc(original.getResourcePath()), ((ModelResourceLocation) loc).getVariant())
                : Constants.loc(original.getResourcePath());
    }
    
    @Override
    public Collection<ResourceLocation> getTextures() {
        Collection<ResourceLocation> textures = Sets.newHashSet();
        try {
            textures.addAll(ModelLoaderRegistry.getModel(original).getTextures());
        } catch (Exception e) {}
        try {
            textures.addAll(ModelLoaderRegistry.getModel(base).getTextures());
        } catch (Exception e) {}
        return ImmutableSet.copyOf(textures);
    }
    
    @Override
    public Collection<ResourceLocation> getDependencies() {
        return ImmutableSet.of(original, base);
    }
    
    @Override
    public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
        IBakedModel missing = ModelLoaderRegistry.getMissingModel().bake(state, format, bakedTextureGetter);
        try {
            return new BakedModelCW(missing, ModelLoaderRegistry.getModel(base), ModelLoaderRegistry.getModel(original));
        } catch (Exception e) {
            CWLogger.logError("Failed loading model " + original + "(" + base + ")", e);
            return missing;
        }
    }
    
}
