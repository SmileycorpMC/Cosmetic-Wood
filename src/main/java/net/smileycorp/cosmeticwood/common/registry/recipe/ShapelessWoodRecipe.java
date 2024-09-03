package net.smileycorp.cosmeticwood.common.registry.recipe;

import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import net.smileycorp.cosmeticwood.api.registry.item.WoodStack;

import javax.annotation.Nonnull;

public class ShapelessWoodRecipe extends ShapelessOreRecipe implements WoodRecipe {
	
	private final IRecipe original;
	
	public ShapelessWoodRecipe(IRecipe recipe) {
		super(new ResourceLocation(recipe.getGroup()), recipe.getRecipeOutput(), getIngredients(recipe));
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
	
	private static Ingredient[] getIngredients(IRecipe recipe) {
		WoodStack output = (WoodStack) (Object) recipe.getRecipeOutput();
		NonNullList<Ingredient> ingredients = NonNullList.withSize(recipe.getIngredients().size(), Ingredient.EMPTY);
		for (int i = 0; i < ingredients.size(); i++) {
			Ingredient ingredient = recipe.getIngredients().get(i);
			ingredients.set(i, ingredient.apply(new ItemStack(Blocks.PLANKS)) ? new WoodOreIngredient("plankWood", output) :
					ingredient.apply(new ItemStack(Blocks.LOG)) ? new WoodOreIngredient("logWood", output) : ingredient);
		}
		return ingredients.toArray(new Ingredient[ingredients.size()]);
	}

}
