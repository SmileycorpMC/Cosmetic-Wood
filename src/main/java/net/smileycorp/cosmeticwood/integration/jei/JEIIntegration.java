package net.smileycorp.cosmeticwood.integration.jei;

import javax.annotation.Nonnull;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.gui.ICraftingGridHelper;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import net.minecraft.item.Item;
import net.smileycorp.cosmeticwood.common.ContentRegistry;
import net.smileycorp.cosmeticwood.common.recipe.ShapedWoodRecipe;
import net.smileycorp.cosmeticwood.common.recipe.ShapelessWoodRecipe;

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
		
		registry.handleRecipes(ShapedWoodRecipe.class, new WoodRecipeHandler<ShapedWoodRecipe>(), VanillaRecipeCategoryUid.CRAFTING);
		registry.handleRecipes(ShapelessWoodRecipe.class, new WoodRecipeHandler<ShapelessWoodRecipe>(), VanillaRecipeCategoryUid.CRAFTING);
	}
	
	@Override
	public void registerItemSubtypes(ISubtypeRegistry registry) {
		for (Item item : ContentRegistry.ITEMS) {
			registry.registerNbtInterpreter(item, CWItemInterpreter.INSTANCE);
		}
	}
	
}
