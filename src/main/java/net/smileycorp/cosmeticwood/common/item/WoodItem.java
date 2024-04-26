package net.smileycorp.cosmeticwood.common.item;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.smileycorp.cosmeticwood.common.CosmeticWood;
import net.smileycorp.cosmeticwood.common.block.WoodBlock;

public interface WoodItem {
    
    @SideOnly(Side.CLIENT)
    default TileEntityItemStackRenderer getITESR() {
        return null;
    }
    
    default boolean isSubtypeTab(CreativeTabs tab) {
        return (tab == CosmeticWood.CREATIVE_TAB);
    }
    
    default <T extends Block & WoodBlock> T block() {
        return (T) ((ItemBlock)this).getBlock();
    }
    
    default ItemStack getStack(ResourceLocation loc) {
        ItemStack stack = new ItemStack((Item)this);
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("type", (loc == null ? getDefaultType() : loc).toString());
        stack.setTagCompound(nbt);
        return stack;
    }
    
    default ResourceLocation getDefaultType() {
        return block().getDefaultType();
    }
    
}
