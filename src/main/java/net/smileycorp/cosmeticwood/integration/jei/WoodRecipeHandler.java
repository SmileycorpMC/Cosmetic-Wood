package net.smileycorp.cosmeticwood.integration.jei;

import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IRecipeWrapperFactory;
import net.smileycorp.cosmeticwood.common.recipe.WoodRecipe;

public class WoodRecipeHandler<T extends WoodRecipe> implements IRecipeWrapperFactory<T> {

	@Override
	public IRecipeWrapper getRecipeWrapper(WoodRecipe recipe) {
		return new WoodRecipeWrapper(recipe);
	}

}
