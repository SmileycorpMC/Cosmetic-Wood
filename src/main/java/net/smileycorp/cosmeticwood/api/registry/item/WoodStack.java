package net.smileycorp.cosmeticwood.api.registry.item;

import net.minecraft.util.ResourceLocation;

public interface WoodStack extends WoodItem {
    
    ResourceLocation getType();
    
}
