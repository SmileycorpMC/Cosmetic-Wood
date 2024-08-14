package net.smileycorp.cosmeticwood.plugins.minecraft;

import net.minecraft.util.ResourceLocation;
import net.smileycorp.cosmeticwood.api.CWPlugin;
import net.smileycorp.cosmeticwood.api.WoodRegistryEntry;

@CWPlugin(modid = "minecraft")
public class PluginVanilla {
    
    public static final WoodRegistryEntry CRAFTING_TABLE = new WoodRegistryEntry.Builder(new ResourceLocation("crafting_table")).build();
    public static final WoodRegistryEntry BOOKSHELF = new WoodRegistryEntry.Builder(new ResourceLocation("bookshelf")).build();
    public static final WoodRegistryEntry BUTTON = new WoodRegistryEntry.Builder(new ResourceLocation("wooden_button")).build();
    public static final WoodRegistryEntry PRESSURE_PLATE = new WoodRegistryEntry.Builder(new ResourceLocation("wooden_pressure_plate")).build();
    
}