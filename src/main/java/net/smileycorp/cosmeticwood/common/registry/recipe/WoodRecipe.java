package net.smileycorp.cosmeticwood.common.registry.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.smileycorp.cosmeticwood.common.data.WoodHandler;
import net.smileycorp.cosmeticwood.common.registry.item.WoodItem;
import net.smileycorp.cosmeticwood.common.registry.item.WoodStack;

public interface WoodRecipe extends IRecipe {
	
	default ItemStack getCraftingResult(InventoryCrafting matrix, ItemStack result) {
		ResourceLocation name = null;
		for (int i = 0; i < matrix.getSizeInventory(); i++) {
			ItemStack stack = matrix.getStackInSlot(i);
			ResourceLocation wood = WoodHandler.getInstance().getRegistry(stack);
			if (wood != null) {
				if (name == null) name = wood;
				else if (!(name.equals(wood))) return WoodItem.getStack(result, ((WoodStack)(Object)result).getDefaultType());
			}
		}
		if (name != null) {
			NBTTagCompound tag = result.hasTagCompound() ? result.getTagCompound() : new NBTTagCompound();
			tag.setString("type", name.toString());
			result.setTagCompound(tag);
		}
		return name == null ? result : WoodItem.getStack(result, name);
	}
}

