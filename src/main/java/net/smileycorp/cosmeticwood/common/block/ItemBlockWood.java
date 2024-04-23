package net.smileycorp.cosmeticwood.common.block;

import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface ItemBlockWood {
    
    @SideOnly(Side.CLIENT)
    default TileEntityItemStackRenderer getITESR() {
        return null;
    }
    
}
