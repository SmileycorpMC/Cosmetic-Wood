package net.smileycorp.cosmeticwood.common.recipe;

import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IShapedRecipe;
import net.minecraftforge.oredict.OreIngredient;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class ShapedWoodRecipe extends ShapedOreRecipe implements WoodRecipe {
	
	private final IShapedRecipe original;
	
	public ShapedWoodRecipe(IShapedRecipe recipe) {
		super(new ResourceLocation(recipe.getGroup()), recipe.getRecipeOutput(), buildRecipe(recipe));
		setRegistryName(recipe.getRegistryName());
		original = recipe;
	}
	
	@Override
	public ItemStack getCraftingResult(InventoryCrafting matrix) {
		return WoodRecipe.super.getCraftingResult(matrix, original.getCraftingResult(matrix));
	}
	
	@Override
	public ItemStack getRecipeOutput(){
		return original.getRecipeOutput();
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
		NonNullList<Ingredient> ingredients = NonNullList.withSize(recipe.getIngredients().size(), Ingredient.EMPTY);
		for (int i = 0; i < ingredients.size(); i++) {
			Ingredient ingredient = recipe.getIngredients().get(i);
			ingredients.set(i, ingredient.apply(new ItemStack(Blocks.PLANKS)) ? new OreIngredient("plankWood") : ingredient);
			ingredients.set(i, ingredient.apply(new ItemStack(Blocks.LOG)) ? new OreIngredient("logWood") : ingredient);
		}
		primer.input = ingredients;
		primer.height = recipe.getRecipeHeight();
		primer.width = recipe.getRecipeWidth();
		return primer;
	}

}

