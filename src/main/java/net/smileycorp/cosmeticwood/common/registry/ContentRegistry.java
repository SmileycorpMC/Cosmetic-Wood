package net.smileycorp.cosmeticwood.common.registry;

import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.crafting.IShapedRecipe;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.smileycorp.cosmeticwood.api.WoodRegistryEntry;
import net.smileycorp.cosmeticwood.api.registry.block.ModifiableWoodBlock;
import net.smileycorp.cosmeticwood.api.registry.item.ModifiableWoodItem;
import net.smileycorp.cosmeticwood.api.registry.item.WoodStack;
import net.smileycorp.cosmeticwood.common.CWLogger;
import net.smileycorp.cosmeticwood.common.Constants;
import net.smileycorp.cosmeticwood.common.registry.recipe.ShapedWoodRecipe;
import net.smileycorp.cosmeticwood.common.registry.recipe.ShapelessWoodRecipe;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileReader;
import java.nio.file.*;
import java.util.Collections;
import java.util.List;


@EventBusSubscriber(modid= Constants.MODID)
public class ContentRegistry {
	
	public static Path CONFIG_FOLDER;
	public static List<Block> BLOCKS = Lists.newArrayList();
	public static List<Item> ITEMS = Lists.newArrayList();
	
	public static void generateData() {
		CONFIG_FOLDER = Paths.get(new File("config/cosmeticwood").getAbsolutePath());
		if (!CONFIG_FOLDER.toFile().exists()) {
			CONFIG_FOLDER.toFile().mkdirs();
			try (FileSystem mod = FileSystems.newFileSystem(ContentRegistry.class.getProtectionDomain().getCodeSource().getLocation().toURI(),
					Collections.emptyMap())) {
				Files.find(mod.getPath("config_defaults"), Integer.MAX_VALUE, (matcher, options) -> options.isRegularFile())
						.forEach(ContentRegistry::copyFileFromMod);
				CWLogger.logInfo("Generated config files");
			} catch (Exception e) {
				CWLogger.logInfo("Failed to generate config files");
			}
		}
	}
	
	public static void init() {
		JsonParser parser = new JsonParser();
		try {
			File plugins = CONFIG_FOLDER.resolve("plugins").toFile();
			for (File file : plugins.listFiles((f, s) -> s.endsWith(".json"))) {
				String name = file.getName().replace(".json", "");
				if (!Loader.isModLoaded(name)) {
					CWLogger.logInfo("Mod " + name + " is not loaded, skipping plugin");
					continue;
				}
				try {
					CWLogger.logInfo("Loading plugin " + name);
					for (JsonElement element : parser.parse(new FileReader(file)).getAsJsonArray()) try {
						WoodRegistryEntry entry = WoodRegistryEntry.fromJson(element);
						if (entry == null) continue;
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
						CWLogger.logInfo("Loaded wood block " + entry.getBlock());
					} catch (Exception e) {
						CWLogger.logError("Failed loading entry " + element.toString(), e);
					}
					CWLogger.logInfo("Loaded plugin " + name);
				} catch (Exception e) {
					CWLogger.logError("Failed loading plugin " + name, e);
				}
			}
		} catch (Exception e) {
			CWLogger.logError("Failed loading plugins", e);
		}
	}
	
	private static void copyFileFromMod(Path path) {
		try {
			FileUtils.copyInputStreamToFile(Files.newInputStream(path),
					new File(CONFIG_FOLDER.toFile(), path.toString().replace( "config_defaults/", "")));
			CWLogger.logInfo("Copied file " + path);
		} catch (Exception e) {
			CWLogger.logError("Failed to copy file " + path, e);
		}
	}
	
	public static void replaceRecipes(){
		IForgeRegistry<IRecipe> recipes = ForgeRegistries.RECIPES;
		CWLogger.logInfo("Replacing recipes");
		recipes.forEach(recipe -> {
			if (!(((WoodStack)(Object)recipe.getRecipeOutput()).isWood())) return;
			CWLogger.logInfo("Replacing recipe " + recipe.getRegistryName());
			recipes.register(recipe instanceof IShapedRecipe ? new ShapedWoodRecipe((IShapedRecipe) recipe) : new ShapelessWoodRecipe(recipe));
		});
	}
	
}
