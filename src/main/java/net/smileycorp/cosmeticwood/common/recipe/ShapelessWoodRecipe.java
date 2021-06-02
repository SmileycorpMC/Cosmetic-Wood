package net.smileycorp.cosmeticwood.common.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class ShapelessWoodRecipe extends ShapelessOreRecipe implements IWoodRecipe {
		
	public ShapelessWoodRecipe(ResourceLocation group, ResourceLocation name, ItemStack result, Object[] recipe) {
		super(group, result, recipe);
		this.setRegistryName(name);
	}
	
	@Override
    public ItemStack getCraftingResult(InventoryCrafting matrix) {
		return IWoodRecipe.super.getCraftingResult(matrix, super.getCraftingResult(matrix));
	}

}
