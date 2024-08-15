package net.smileycorp.cosmeticwood.client;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
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
import net.smileycorp.cosmeticwood.common.data.WoodHandler;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

public class ModelCW implements IModel {
    
    private final ResourceLocation original, base;
    private Map<ResourceLocation, IModel> submodels = null;
    
    public ModelCW(ResourceLocation loc) {
        this.original = loc;
        base = loc instanceof ModelResourceLocation ? new ModelResourceLocation(Constants.loc(original.getResourceDomain() + "/" + original.getResourcePath()),
                ((ModelResourceLocation) loc).getVariant())
                : Constants.loc(original.getResourcePath().replaceFirst("/", "/" + original.getResourceDomain() + "/"));
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
        if (submodels == null) buildSubmodels();
        for (IModel model : submodels.values()) textures.addAll(model.getTextures());
        return ImmutableSet.copyOf(textures);
    }
    
    private void buildSubmodels() {
        submodels = Maps.newHashMap();
        for (ResourceLocation type : WoodHandler.getInstance().getTypes(null)) {
            try {
                ResourceLocation loc =  new ResourceLocation(base.getResourceDomain(), base.getResourcePath() + "_"
                        + type.getResourceDomain() + "_" + type.getResourcePath());
                if (base instanceof ModelResourceLocation) loc = new ModelResourceLocation(loc, ((ModelResourceLocation) base).getVariant());
                submodels.put(type, ModelLoaderRegistry.getModel(loc));
            } catch (Exception e) {}
        }
    }
    
    @Override
    public Collection<ResourceLocation> getDependencies() {
        return ImmutableSet.of(original, base);
    }
    
    @Override
    public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
        IBakedModel missing = ModelLoaderRegistry.getMissingModel().bake(state, format, bakedTextureGetter);
        if (submodels == null) buildSubmodels();
        try {
            return new BakedModelCW(missing, ModelLoaderRegistry.getModel(base), ModelLoaderRegistry.getModel(original), submodels);
        } catch (Exception e) {
            CWLogger.logError("Failed loading model " + original + "(" + base + ")", e);
            return missing;
        }
    }
    
}
