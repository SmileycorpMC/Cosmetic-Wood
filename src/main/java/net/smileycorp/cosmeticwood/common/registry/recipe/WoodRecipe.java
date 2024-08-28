package net.smileycorp.cosmeticwood.common.registry.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.smileycorp.cosmeticwood.api.registry.item.WoodItem;
import net.smileycorp.cosmeticwood.api.registry.item.WoodStack;
import net.smileycorp.cosmeticwood.common.data.WoodHandler;

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
		return name == null ? result : WoodItem.getStack(result, name);
	}
}

