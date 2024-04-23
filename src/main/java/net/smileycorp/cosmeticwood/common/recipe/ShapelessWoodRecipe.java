package net.smileycorp.cosmeticwood.common.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import javax.annotation.Nonnull;

public class ShapelessWoodRecipe extends ShapelessOreRecipe implements IWoodRecipe {
	
	private final IRecipe original;
	
	public ShapelessWoodRecipe(IRecipe recipe) {
		super(new ResourceLocation(recipe.getGroup()), recipe.getRecipeOutput(), recipe.getIngredients().toArray());
		setRegistryName(recipe.getRegistryName());
		original = recipe;
	}
	
	@Override
	public ItemStack getCraftingResult(InventoryCrafting matrix) {
		return IWoodRecipe.super.getCraftingResult(matrix, original.getCraftingResult(matrix));
	}
	
	@Override
	public ItemStack getRecipeOutput(){
		return original.getRecipeOutput();
	}
	
	@Override
	public boolean matches(@Nonnull InventoryCrafting inv, @Nonnull World world) {
		return original.matches(inv, world);
	}
	
	@Override
	public NonNullList<Ingredient> getIngredients() {
		return original.getIngredients();
	}
	
	@Override
	public boolean canFit(int width, int height) {
		return original.canFit(width, height);
	}

}
