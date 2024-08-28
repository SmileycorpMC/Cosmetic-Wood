package net.smileycorp.cosmeticwood.api.registry;

import net.minecraft.util.ResourceLocation;

public interface WoodObject {
    
    boolean isWood();
    
    ResourceLocation getDefaultType();
    
    String[] getModIds();
    
}
