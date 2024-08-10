package net.smileycorp.cosmeticwood.common.registry.item;

import net.minecraft.util.ResourceLocation;

public interface ModifiableWoodItem extends WoodItem {
    
    void setWoodItem();
    
    void setDefault(ResourceLocation loc);
    
    void setModIds(String... modids);
    
}
