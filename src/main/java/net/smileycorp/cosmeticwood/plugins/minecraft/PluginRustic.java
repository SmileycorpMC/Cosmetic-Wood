package net.smileycorp.cosmeticwood.plugins.minecraft;

import net.minecraft.util.ResourceLocation;
import net.smileycorp.cosmeticwood.api.CWPlugin;
import net.smileycorp.cosmeticwood.api.WoodRegistryEntry;

@CWPlugin(modid = "rustic")
public class PluginRustic {
    
    public static final WoodRegistryEntry CHAIR = new WoodRegistryEntry.Builder(new ResourceLocation("rustic:chair_oak"))
            .exclude("minecraft", "rustic").build();
    
    public static final WoodRegistryEntry TABLE = new WoodRegistryEntry.Builder(new ResourceLocation("rustic:table_oak"))
            .exclude("minecraft", "rustic").build();
    
}