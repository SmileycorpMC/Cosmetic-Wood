package net.smileycorp.cosmeticwood.plugins.minecraft;

import net.minecraft.util.ResourceLocation;
import net.smileycorp.cosmeticwood.api.CWPlugin;
import net.smileycorp.cosmeticwood.api.WoodRegistryEntry;

@CWPlugin
public class PluginVanilla {
    
    public static final WoodRegistryEntry CRAFTING_TABLE = new WoodRegistryEntry.Builder(new ResourceLocation("crafting_table")).build();
    public static final WoodRegistryEntry BOOKSHELF = new WoodRegistryEntry.Builder(new ResourceLocation("bookshelf")).build();
    
}
