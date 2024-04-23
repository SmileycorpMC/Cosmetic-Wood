package net.smileycorp.cosmeticwood.plugins.vanilla.block;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.smileycorp.cosmeticwood.common.block.ItemBlockSimpleWood;
import net.smileycorp.cosmeticwood.plugins.vanilla.client.TESRCWChest;

public class ItemBlockChestCW extends ItemBlockSimpleWood {
    
    public ItemBlockChestCW(Block block) {
        super(block);
    }
    
    @SideOnly(Side.CLIENT)
    public TileEntityItemStackRenderer getITESR() {
        return new TESRCWChest.Item();
    }
    
}
