package net.smileycorp.cosmeticwood.common.block;

import net.minecraft.util.ResourceLocation;

public interface ModifiableWoodBlock extends WoodBlock {
    
    void setWoodBlock();
    
    void setDefault(ResourceLocation loc);
    
    void setModIds(String... modids);
    
}
