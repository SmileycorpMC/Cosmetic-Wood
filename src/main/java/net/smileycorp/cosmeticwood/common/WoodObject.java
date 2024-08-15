package net.smileycorp.cosmeticwood.common;

import net.minecraft.util.ResourceLocation;

public interface WoodObject {
    
    boolean isWood();
    
    ResourceLocation getDefaultType();
    
    String[] getModIds();
    
}
