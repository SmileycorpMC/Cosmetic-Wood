package net.smileycorp.cosmeticwood.client;

import com.google.common.collect.Maps;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.IRegistry;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.smileycorp.atlas.api.client.TextureAtlasGreyscale;
import net.smileycorp.cosmeticwood.common.CWLogger;
import net.smileycorp.cosmeticwood.common.Constants;
import net.smileycorp.cosmeticwood.common.data.WoodHandler;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CWModelLoader {
    
    private static final Map<ModelResourceLocation, ResourceLocation> MODELS = Maps.newHashMap();
    private static Map<String, TextureAtlasSprite> GREYSCALE_SPRITES = Maps.newHashMap();
    
    public static void registerModels(Collection<? extends ResourceLocation> locs) {
        locs.forEach(CWModelLoader::registerModel);
    }
    
    public static void registerModels(ResourceLocation... locs) {
        for (ResourceLocation loc : locs) registerModel(loc);
    }
    
    public static void registerModel(ResourceLocation loc) {
        if (!(loc instanceof ModelResourceLocation)) return;
        ModelResourceLocation original = (ModelResourceLocation) loc;
        ResourceLocation base = original.getVariant().equals("inventory") ?
                Constants.loc( "item/" + original.getResourceDomain() + "/" + original.getResourcePath()):
                new ModelResourceLocation(Constants.loc(original.getResourceDomain() + "/" + original.getResourcePath()), original.getVariant());
        MODELS.put(original, base);
        CWLogger.logInfo("Registered wood model " + loc);
    }
    
    public static void bakeModels(IRegistry<ModelResourceLocation, IBakedModel> registry) {
        for (Map.Entry<ModelResourceLocation, ResourceLocation> entry : MODELS.entrySet())
            registry.putObject(entry.getKey(), getModel(registry.getObject(entry.getKey()), entry.getValue()));
    }
    
    public static IBakedModel getModel(IBakedModel original, ResourceLocation base) {
        HashMap<ResourceLocation, IModel> submodels = Maps.newHashMap();
        for (ResourceLocation type : WoodHandler.getInstance().getTypes(null)) {
            try {
                ResourceLocation loc = Constants.loc(base.getResourcePath() + "_"
                        + type.getResourceDomain() + "_" + type.getResourcePath());
                if (base instanceof ModelResourceLocation) loc = new ModelResourceLocation(loc, ((ModelResourceLocation)base).getVariant());
                IModel model = ModelLoaderRegistry.getModelOrMissing(loc);
                if (model == ModelLoaderRegistry.getMissingModel()) continue;
                submodels.put(type, model);
            } catch (Exception e) {}
        }
        try {
            return new BakedModelCW(original, ModelLoaderRegistry.getModel(base), submodels);
        } catch (Exception e) {
            CWLogger.logError("Failed loading model " + base, e);
            return original;
        }
    }
    
    public static void stitchTextures(TextureMap map) {
        stitchGreyscale(map, "plank", new ResourceLocation("minecraft", "blocks/planks_oak"));
        stitchGreyscale(map, "log_top", new ResourceLocation("minecraft", "blocks/log_oak_top"));
        stitchGreyscale(map, "log_side", new ResourceLocation("minecraft", "blocks/log_oak"));
        for (ResourceLocation base : MODELS.values()) {
            for (ResourceLocation type : WoodHandler.getInstance().getTypes(null)) {
                try {
                    ResourceLocation loc = Constants.loc(base.getResourcePath() + "_"
                            + type.getResourceDomain() + "_" + type.getResourcePath());
                    if (base instanceof ModelResourceLocation) loc = new ModelResourceLocation(loc, ((ModelResourceLocation) base).getVariant());
                    IModel model = ModelLoaderRegistry.getModelOrMissing(loc);
                    if (model == ModelLoaderRegistry.getMissingModel()) continue;
                    stitchTextures(map, model);
                } catch (Exception e) {}
            }
            try {
                stitchTextures(map, ModelLoaderRegistry.getModel(base));
            } catch (Exception e) {}
        }
    }
    
    private static void stitchGreyscale(TextureMap map, String key, ResourceLocation registry) {
        TextureAtlasSprite sprite = new TextureAtlasGreyscale(registry);
        map.setTextureEntry(sprite);
        GREYSCALE_SPRITES.put(key, sprite);
    }
    
    private static void stitchTextures(TextureMap map, IModel model) {
        for (ResourceLocation texture : model.getTextures()) map.registerSprite(texture);
    }
    
    public static TextureAtlasSprite getGreyscaleSprite(String key) {
        return GREYSCALE_SPRITES.get(key);
    }
    
}
