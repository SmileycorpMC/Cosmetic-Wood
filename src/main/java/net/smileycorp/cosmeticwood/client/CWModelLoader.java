package net.smileycorp.cosmeticwood.client;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
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
import java.util.Set;

public class CWModelLoader {
    
    private static final Set<ModelResourceLocation> MODELS = Sets.newHashSet();
    private static Map<String, TextureAtlasSprite> GREYSCALE_SPRITES = new HashMap<String, TextureAtlasSprite>();
    
    public static void registerModels(Collection<? extends ResourceLocation> locs) {
        for (ResourceLocation loc : locs) registerModel(loc);
    }
    
    public static void registerModels(ResourceLocation... locs) {
        for (ResourceLocation loc : locs) registerModel(loc);
    }
    
    public static void registerModel(ResourceLocation loc) {
        if (!(loc instanceof ModelResourceLocation)) return;
        MODELS.add((ModelResourceLocation) loc);
        CWLogger.logInfo("Registered wood model " + loc);
    }
    
    public static void bakeModels(IRegistry<ModelResourceLocation, IBakedModel> registry) {
        for (ModelResourceLocation loc : MODELS) registry.putObject(loc, getModel(registry.getObject(loc), loc));
    }
    
    public static IBakedModel getModel(IBakedModel original, ModelResourceLocation base) {
        HashMap<ResourceLocation, IModel> submodels = Maps.newHashMap();
        for (ResourceLocation type : WoodHandler.getInstance().getTypes(null)) {
            try {
                ResourceLocation loc =  new ResourceLocation(base.getResourceDomain(), base.getResourcePath() + "_"
                        + type.getResourceDomain() + "_" + type.getResourcePath());
                if (base instanceof ModelResourceLocation) loc = new ModelResourceLocation(loc, base.getVariant());
                IModel model = ModelLoaderRegistry.getModelOrMissing(loc);
                if (model == ModelLoaderRegistry.getMissingModel()) continue;
                submodels.put(type, model);
            } catch (Exception e) {}
        }
        try {
            return new BakedModelCW(original, ModelLoaderRegistry.getModel(new ModelResourceLocation(Constants.loc(base.getResourceDomain() + "/" + base.getResourcePath()),
                    base.getVariant())), submodels);
        } catch (Exception e) {
            CWLogger.logError("Failed loading model " + base, e);
            return original;
        }
    }
    
    public static void stitchTextures(TextureMap map) {
        stitchGreyscale(map, "plank", new ResourceLocation("minecraft", "blocks/planks_oak"));
        stitchGreyscale(map, "log_top", new ResourceLocation("minecraft", "blocks/log_oak_top"));
        stitchGreyscale(map, "log_side", new ResourceLocation("minecraft", "blocks/log_oak"));
        for (ModelResourceLocation base : MODELS) {
            for (ResourceLocation type : WoodHandler.getInstance().getTypes(null)) {
                try {
                    ResourceLocation loc = new ResourceLocation(base.getResourceDomain(), base.getResourcePath() + "_"
                            + type.getResourceDomain() + "_" + type.getResourcePath());
                    if (base instanceof ModelResourceLocation) loc = new ModelResourceLocation(loc, base.getVariant());
                    IModel model = ModelLoaderRegistry.getModelOrMissing(loc);
                    if (model == ModelLoaderRegistry.getMissingModel()) continue;
                    stitchTextures(map, model);
                } catch (Exception e) {}
            }
            try {
                stitchTextures(map, ModelLoaderRegistry.getModel(new ModelResourceLocation(Constants.loc(base.getResourceDomain() + "/" + base.getResourcePath()),
                        base.getVariant())));
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
