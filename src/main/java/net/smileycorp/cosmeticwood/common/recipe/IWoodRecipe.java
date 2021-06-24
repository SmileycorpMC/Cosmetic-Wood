package net.smileycorp.cosmeticwood.common.recipe;

import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.smileycorp.cosmeticwood.common.ContentRegistry;
import net.smileycorp.cosmeticwood.common.WoodHandler;

public interface IWoodRecipe {
	public default ItemStack getCraftingResult(InventoryCrafting matrix, ItemStack result) {
		ResourceLocation name = null;
		for (int i = 0; i < matrix.getSizeInventory(); i++) {
			ItemStack stack = matrix.getStackInSlot(i);
			ResourceLocation wood = WoodHandler.getRegistry(stack);
			if (ContentRegistry.ITEMS.contains(stack.getItem())) {
				NBTTagCompound nbt = stack.getTagCompound();
				if (nbt!=null) {
					if (nbt.hasKey("type")) {
						wood = WoodHandler.fixData(nbt.getString("type"));
					}
				}	
			}
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
			tag.setString("type", name.toString());
			result.setTagCompound(tag);
		}
		return result;
	}
}

