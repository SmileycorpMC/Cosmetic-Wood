package net.smileycorp.cosmeticwood.plugins.minecraft;

import net.minecraft.util.ResourceLocation;
import net.smileycorp.cosmeticwood.api.CWPlugin;
import net.smileycorp.cosmeticwood.api.WoodRegistryEntry;

@CWPlugin(modid = "tconstruct")
public class PluginTCon {
    
    public static final WoodRegistryEntry WOODEN_HOPPER = new WoodRegistryEntry.Builder(new ResourceLocation("tconstruct:wooden_hopper")).build();
    
}