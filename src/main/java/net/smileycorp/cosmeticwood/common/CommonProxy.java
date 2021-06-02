package net.smileycorp.cosmeticwood.common;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import net.minecraftforge.registries.IForgeRegistryModifiable;
import net.smileycorp.cosmeticwood.common.block.CWBlocks;
import net.smileycorp.cosmeticwood.common.recipe.ShapedWoodRecipe;
import net.smileycorp.cosmeticwood.common.recipe.ShapelessWoodRecipe;

@EventBusSubscriber(modid = ModDefinitions.modid)
public class CommonProxy {
	
	public void preInit(FMLPreInitializationEvent event) {
	}

	public void init(FMLInitializationEvent event) {
		WoodHandler.buildProperties();
	}

	public void postInit(FMLPostInitializationEvent event) {
		
		
	}
	
	@SubscribeEvent
	public static void recipeWriter(RegistryEvent.Register<IRecipe> event){
		IForgeRegistryModifiable<IRecipe> recipes = (IForgeRegistryModifiable<IRecipe>) event.getRegistry();
		for (Block block : CWBlocks.blocks) {
			ResourceLocation key = block.getRegistryName();
			if (recipes.containsKey(key)) {
				IRecipe recipe = recipes.getValue(key);
				if (recipe instanceof ShapedOreRecipe) {
					Object[] ingredients = buildRecipe(recipe.getIngredients(), ((ShapedOreRecipe)recipe).getWidth());
					recipe = new ShapedWoodRecipe(ModDefinitions.getRegistry(recipe.getGroup()),  key, new ItemStack(block), ingredients);
				} else if (recipe instanceof ShapedRecipes) {
					Object[] ingredients = buildRecipe(recipe.getIngredients(), ((ShapedRecipes)recipe).getWidth());
					recipe = new ShapedWoodRecipe(ModDefinitions.getRegistry(recipe.getGroup()),  key, new ItemStack(block), ingredients);
				} else if (recipe instanceof ShapelessOreRecipe || recipe instanceof ShapelessRecipes) {
					recipe = new ShapelessWoodRecipe(ModDefinitions.getRegistry(recipe.getGroup()),  key, new ItemStack(block), recipe.getIngredients().toArray());
				}
				recipes.remove(key);
				recipes.register(recipe);
				//System.out.println("Replaced recipe for " + key);
			}
			
		}
	}
	
	private static Object[] buildRecipe(NonNullList<Ingredient> ingredients, final int width) {
		List<String> pattern = new ArrayList<String>();
		List<Object> newIngredients = new ArrayList<Object>();
		int pos = 0;
		int i = 0;
		for (Ingredient ingredient : ingredients) {
			if (ingredient.apply(new ItemStack(Blocks.AIR))) {
				pos = addToPattern(pattern, ' ', width , pos);
			} else if (newIngredients.isEmpty()) {
				pos = addToPattern(pattern, (char)(i+65), width , pos);
				newIngredients.add((char)(i+65));
				newIngredients.add(ingredient);
				i++;
			} else {
				for (Object newIngredient : newIngredients) {
					if (newIngredient instanceof Ingredient) {
						if (ingredient.equals(newIngredient)) {
							pos = addToPattern(pattern, (char)(newIngredients.lastIndexOf(ingredient)+65), width, pos);
							i++;
							break;
						}
						pos = addToPattern(pattern, (char)(i+65), width , pos);
						newIngredients.add((char)(i+65));
						newIngredients.add(ingredient);
						i++;
						break;
					}
				}
			}
		}
		ArrayList<Object> result = new ArrayList<Object>();
		result.addAll(pattern);
		result.addAll(newIngredients);
		return result.toArray();
	}

	private static int addToPattern(List<String> pattern, char character, final int width, int position) {
		if (position < width && pattern.size()>0) {
			int index = pattern.size()-1;
			String str = pattern.get(index);
			str += character;
			pattern.add(str);
			pattern.remove(index);
		} else {
			pattern.add(String.valueOf(character));
			position = 0;
		}
		position++;
		return position;
	}

	/*private static void addRecipe(ResourceLocation base, String type, ItemStack output, Object... pattern) {
		ResourceLocation registry = new ResourceLocation(base.getResourceDomain(), base.getResourcePath()+"_"+type);
		NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("type", type);
        output.setTagCompound(nbt);
		GameRegistry.addShapedRecipe(registry, base, output, pattern);
	}*/
}
