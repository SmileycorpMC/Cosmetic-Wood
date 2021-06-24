package net.smileycorp.cosmeticwood.integration.jei;

import net.smileycorp.cosmeticwood.common.recipe.IWoodRecipe;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IRecipeWrapperFactory;

public class WoodRecipeHandler<T extends IWoodRecipe> implements IRecipeWrapperFactory<T> {

	@Override
	public IRecipeWrapper getRecipeWrapper(IWoodRecipe recipe) {
		return new WoodRecipeWrapper(recipe);
	}

}
