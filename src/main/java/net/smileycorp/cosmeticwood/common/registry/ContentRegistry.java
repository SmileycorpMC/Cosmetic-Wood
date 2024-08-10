package net.smileycorp.cosmeticwood.common.registry;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.crafting.IShapedRecipe;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.discovery.ASMDataTable.ASMData;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.smileycorp.cosmeticwood.api.CWPlugin;
import net.smileycorp.cosmeticwood.api.WoodRegistryEntry;
import net.smileycorp.cosmeticwood.common.CWLogger;
import net.smileycorp.cosmeticwood.common.Constants;
import net.smileycorp.cosmeticwood.common.registry.block.ModifiableWoodBlock;
import net.smileycorp.cosmeticwood.common.registry.item.ModifiableWoodItem;
import net.smileycorp.cosmeticwood.common.registry.item.WoodItem;
import net.smileycorp.cosmeticwood.common.registry.recipe.ShapedWoodRecipe;
import net.smileycorp.cosmeticwood.common.registry.recipe.ShapelessWoodRecipe;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;


@EventBusSubscriber(modid= Constants.MODID)
public class ContentRegistry {
	
	public static List<Class> PLUGINS = Lists.newArrayList();
	public static List<Block> BLOCKS = Lists.newArrayList();
	public static List<Item> ITEMS = Lists.newArrayList();
	
	public static void preInit(ASMDataTable asmtable) {
		String annotation = CWPlugin.class.getCanonicalName();
		Set<ASMData> dataset = asmtable.getAll(annotation);
		for (ASMData data : dataset) {
			String modid = (String) data.getAnnotationInfo().get("modid");
			if (Loader.isModLoaded(modid)) {
				try {
					Class plugin = Class.forName(data.getClassName());
					CWLogger.logInfo("Loading plugin " + modid);
					PLUGINS.add(plugin);
				} catch (Exception e) {
					CWLogger.logError("Error loading plugin " + modid, e);
				}
			} else CWLogger.logInfo("Mod " + modid + " not detected. Skipping plugin.");
		}
	}
	
	public static void init() {
		for (Class<?> plugin : PLUGINS) {
			Field[] fields = plugin.getFields();
			for (Field field : fields) try {
				Object o = field.get(new Object());
				if (o != null && o instanceof WoodRegistryEntry) {
					WoodRegistryEntry entry = (WoodRegistryEntry) o;
					if (entry.getBlock() != null) {
						ModifiableWoodBlock block = (ModifiableWoodBlock) ForgeRegistries.BLOCKS.getValue(entry.getBlock());
						block.setWoodBlock();
						block.setDefault(entry.getDefaultType());
						block.setModIds(entry.getExcludedModids().toArray(new String[]{}));
						BLOCKS.add((Block) block);
					}
					if (entry.getItem() != null) {
						ModifiableWoodItem item = (ModifiableWoodItem) ForgeRegistries.ITEMS.getValue(entry.getBlock());
						item.setWoodItem();
						item.setDefault(entry.getDefaultType());
						item.setModIds(entry.getExcludedModids().toArray(new String[]{}));
						ITEMS.add((Item) item);
					}
				}
			} catch (Exception e) {
				CWLogger.logError("Failed loading entry " + field.getName(), e);
			}
		}
	}
	
	public static void replaceRecipes(){
		IForgeRegistry<IRecipe> recipes = ForgeRegistries.RECIPES;
		CWLogger.logInfo("Replacing recipes");
		recipes.forEach(recipe -> {
			if (!(((WoodItem)recipe.getRecipeOutput().getItem()).isWoodItem())) return;
			CWLogger.logInfo("Replacing recipe " + recipe.getRegistryName());
			recipes.register(recipe instanceof IShapedRecipe ? new ShapedWoodRecipe((IShapedRecipe) recipe) : new ShapelessWoodRecipe(recipe));
		});
	}
	
}
