package net.smileycorp.cosmeticwood.common.block;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import java.util.function.Supplier;

public interface ModifiableWoodBlock extends WoodBlock {
    
    void setWoodBlock();
    
    void setDefault(ResourceLocation loc);
    
    void setTileEntity(Supplier<TileEntity> supplier);
    
    void setModIds(String... modids);
    
}
