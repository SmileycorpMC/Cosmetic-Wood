package net.smileycorp.cosmeticwood.api.registry.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.smileycorp.cosmeticwood.api.registry.WoodObject;

public interface WoodItem extends WoodObject {
    
    ResourceLocation getType(ItemStack stack);
    
    static ItemStack getStack(Item item, ResourceLocation loc) {
        return getStack(new ItemStack(item), loc);
    }
    
    static ItemStack getStack(ItemStack stack, ResourceLocation loc) {
        if (loc == null || ((WoodStack)(Object)stack).getDefaultType().equals(loc)) return stack;
        NBTTagCompound nbt = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
        nbt.setString("type", (loc == null ? ((WoodStack)(Object)stack).getDefaultType() : loc).toString());
        stack.setTagCompound(nbt);
        return stack;
    }
    
}
