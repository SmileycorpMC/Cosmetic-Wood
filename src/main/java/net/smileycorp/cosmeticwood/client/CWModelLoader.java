package net.smileycorp.cosmeticwood.client;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;

public class CWModelLoader implements ICustomModelLoader {
    
    @Override
    public boolean accepts(ResourceLocation loc) {
        return loc instanceof ModelResourceLocation && loc.getResourcePath().contains(".wood");
    }
    
    @Override
    public IModel loadModel(ResourceLocation location) throws Exception {
        return new ModelCW(location.getResourcePath().contains("item") ? new ResourceLocation(location.getResourceDomain(),
                "item/" + location.getResourcePath().split("\\.")[0])
            : new ModelResourceLocation(new ResourceLocation(location.getResourceDomain(),
                location.getResourcePath().split("\\.")[0]), ((ModelResourceLocation)location).getVariant()));
    }
    
    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {}
    
}
