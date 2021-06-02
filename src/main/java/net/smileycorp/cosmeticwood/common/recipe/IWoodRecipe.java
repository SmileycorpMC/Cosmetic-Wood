package net.smileycorp.cosmeticwood.common.recipe;

import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.smileycorp.cosmeticwood.common.WoodHandler;

public interface IWoodRecipe {
	public default ItemStack getCraftingResult(InventoryCrafting matrix, ItemStack result) {
		String name = null;
		for (int i = 0; i < matrix.getSizeInventory(); i++) {
			ItemStack stack = matrix.getStackInSlot(i);
			String wood = WoodHandler.getName(stack);
			if (wood != null) {
				if (name == null) {
					name = wood;
				}  else if (!(name.equals(wood))) {
					return new ItemStack(Blocks.AIR);
				}
			}
		}
		if  (name!=null) {
			NBTTagCompound tag = new NBTTagCompound();
			tag.setString("type", name);
			result.setTagCompound(tag);
		}
		return result;
	}
}

