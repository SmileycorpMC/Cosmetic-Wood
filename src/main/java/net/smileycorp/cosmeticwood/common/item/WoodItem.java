package net.smileycorp.cosmeticwood.common.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public interface WoodItem {
    
    boolean isWoodItem();
    
    ResourceLocation getDefaultType();
    
    ResourceLocation getType(ItemStack stack);
    
    static ItemStack getStack(Item item, ResourceLocation loc) {
        ItemStack stack = new ItemStack(item);
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("type", (loc == null ? ((WoodItem)item).getDefaultType() : loc).toString());
        stack.setTagCompound(nbt);
        return stack;
    }
    
    static ItemStack getStack(ItemStack stack, ResourceLocation loc) {
        NBTTagCompound nbt = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
        nbt.setString("type", (loc == null ? ((WoodStack)stack).getDefaultType() : loc).toString());
        stack.setTagCompound(nbt);
        return stack;
    }
    
    String[] getModIds();
    
}
