package net.smileycorp.cosmeticwood.integration.jei;

import mezz.jei.api.*;
import mezz.jei.api.gui.ICraftingGridHelper;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import net.minecraft.item.Item;
import net.smileycorp.cosmeticwood.common.ContentRegistry;
import net.smileycorp.cosmeticwood.common.recipe.ShapedWoodRecipe;
import net.smileycorp.cosmeticwood.common.recipe.ShapelessWoodRecipe;

import javax.annotation.Nonnull;

@JEIPlugin
public class JEIIntegration implements IModPlugin {
	
	//static IRecipeRegistry registry;
	static IJeiHelpers helpers;
	static ICraftingGridHelper craftingHelper;

	@Override
	public void onRuntimeAvailable(IJeiRuntime runtime) {
		//registry = runtime.getRecipeRegistry();
	}
	
	@Override
	public void register(@Nonnull IModRegistry registry) {
		helpers = registry.getJeiHelpers();
		craftingHelper = helpers.getGuiHelper().createCraftingGridHelper(1, 0);
		registry.handleRecipes(ShapedWoodRecipe.class, new WoodRecipeHandler<>(), VanillaRecipeCategoryUid.CRAFTING);
		registry.handleRecipes(ShapelessWoodRecipe.class, new WoodRecipeHandler<>(), VanillaRecipeCategoryUid.CRAFTING);
	}
	
	@Override
	public void registerItemSubtypes(ISubtypeRegistry registry) {
		for (Item item : ContentRegistry.ITEMS) {
			registry.registerNbtInterpreter(item, CWItemInterpreter.INSTANCE);
		}
	}
	
}
