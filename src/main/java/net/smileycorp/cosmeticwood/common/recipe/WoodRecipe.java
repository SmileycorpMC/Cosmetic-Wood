package net.smileycorp.cosmeticwood.common.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.smileycorp.cosmeticwood.common.WoodHandler;
import net.smileycorp.cosmeticwood.common.block.WoodBlock;

public interface WoodRecipe extends IRecipe {
	
	default ItemStack getCraftingResult(InventoryCrafting matrix, ItemStack result) {
		ResourceLocation name = null;
		for (int i = 0; i < matrix.getSizeInventory(); i++) {
			ItemStack stack = matrix.getStackInSlot(i);
			ResourceLocation wood = WoodHandler.getRegistry(stack);
			if (wood != null) {
				if (name == null) name = wood;
				else if (!(name.equals(wood) || stack.getItem() instanceof WoodBlock)) {
					NBTTagCompound tag = result.hasTagCompound() ? result.getTagCompound() : new NBTTagCompound();
					tag.setString("type", WoodHandler.getDefault(result).toString());
					result.setTagCompound(tag);
					return result;
				}
			}
		}
		if (name != null) {
			NBTTagCompound tag = result.hasTagCompound() ? result.getTagCompound() : new NBTTagCompound();
			tag.setString("type", name.toString());
			result.setTagCompound(tag);
		}
		return result;
	}
}

