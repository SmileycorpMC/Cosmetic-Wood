package net.smileycorp.cosmeticwood.common.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IShapedRecipe;
import net.minecraftforge.oredict.ShapedOreRecipe;

import javax.annotation.Nonnull;

public class ShapedWoodRecipe extends ShapedOreRecipe implements IWoodRecipe {
	
	private final IShapedRecipe original;
	
	public ShapedWoodRecipe(IShapedRecipe recipe) {
		super(new ResourceLocation(recipe.getGroup()), recipe.getRecipeOutput(), buildRecipe(recipe));
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
	public int getHeight() {
		return original.getRecipeHeight();
	}
	
	@Override
	public int getWidth() {
		return original.getRecipeWidth();
	}
	
	@Override
	public boolean canFit(int width, int height) {
		return original.canFit(width, height);
	}
	
	private static CraftingHelper.ShapedPrimer buildRecipe(IShapedRecipe recipe) {
		CraftingHelper.ShapedPrimer primer = new CraftingHelper.ShapedPrimer();
		primer.input = recipe.getIngredients();
		primer.height = recipe.getRecipeHeight();
		primer.width = recipe.getRecipeWidth();
		return primer;
	}

}

