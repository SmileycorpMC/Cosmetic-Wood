package net.smileycorp.cosmeticwood.common.item;

import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.smileycorp.cosmeticwood.common.CosmeticWood;

public interface ItemBlockWood {
    
    @SideOnly(Side.CLIENT)
    default TileEntityItemStackRenderer getITESR() {
        return null;
    }
    
    default boolean isSubtypeTab(CreativeTabs tab) {
        return (tab == CosmeticWood.CREATIVE_TAB);
    }
    
}
