package net.smileycorp.cosmeticwood.common;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.discovery.ASMDataTable.ASMData;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import net.minecraftforge.registries.IForgeRegistryModifiable;
import net.smileycorp.cosmeticwood.common.block.IWoodBlock;
import net.smileycorp.cosmeticwood.common.recipe.ShapedWoodRecipe;
import net.smileycorp.cosmeticwood.common.recipe.ShapelessWoodRecipe;
import net.smileycorp.cosmeticwood.common.tile.ITileCW;


@EventBusSubscriber(modid=ModDefinitions.modid)
public class ContentRegistry {
	
	public static List<Class> PLUGINS = new ArrayList<Class>();
	public static List<Block> BLOCKS = new ArrayList<Block>();
	public static List<Item> ITEMS = new ArrayList<Item>();
	public static List<Class<? extends TileEntity>> TILE_ENTITIES = new ArrayList<Class<? extends TileEntity>>();
	
	public static void preInit(ASMDataTable asmtable) {
		String annotation = CWPlugin.class.getCanonicalName();
		Set<ASMData> dataset = asmtable.getAll(annotation);
		for (ASMData data : dataset) {
			String modid = (String) data.getAnnotationInfo().get("modid");
			if (Loader.isModLoaded(modid)) {
				List<Block> blocks = new ArrayList<Block>();
				try {
					Class plugin = Class.forName(data.getClassName());
					CosmeticWood.logInfo("Loading plugin " + modid);
					Field[] fields = plugin.getFields();
					for (Field field : fields) {
						Object o = field.get(new Object());
						if (o!=null && o instanceof Block && o instanceof IWoodBlock) {
							blocks.add((Block) o);
						}
					}
					PLUGINS.add(plugin);
					BLOCKS.addAll(blocks);
				} catch (Exception e) {
					CosmeticWood.logError("Error loading plugin " + modid, e);
				}
			} else {
				CosmeticWood.logInfo("Mod " + modid + " not detected. Skipping plugin.");
			}
		}
		for (Block block : BLOCKS) {
			if (block instanceof IWoodBlock) {
				Item item = ((IWoodBlock)block).getItem();
				ITEMS.add(item);
				Class tile = ((IWoodBlock)block).getTile();
				if (!TILE_ENTITIES.contains(tile)) {
					try {
						GameRegistry.registerTileEntity(tile, ((ITileCW) tile.newInstance()).getRegistryName());
					} catch (Exception e) {}
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event){
		ForgeRegistries.BLOCKS.registerAll(BLOCKS.toArray(new Block[] {}));
	}
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event){
		ForgeRegistries.ITEMS.registerAll(ITEMS.toArray(new Item[] {}));
	}
	
	@SubscribeEvent
	public static void recipeWriter(RegistryEvent.Register<IRecipe> event){
		IForgeRegistryModifiable<IRecipe> recipes = (IForgeRegistryModifiable<IRecipe>) event.getRegistry();
		for (Block block : ContentRegistry.BLOCKS) {
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
			}
			
		}
	}
	
	private static Object[] buildRecipe(NonNullList<Ingredient> ingredients, final int width) {
		List<String> pattern = new ArrayList<>();
		List<Object> newIngredients = new ArrayList<>();
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
		ArrayList<Object> result = new ArrayList<>();
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
	
}
