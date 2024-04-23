package net.smileycorp.cosmeticwood.common;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.crafting.IShapedRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.discovery.ASMDataTable.ASMData;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.smileycorp.cosmeticwood.common.block.ItemBlockSimpleWood;
import net.smileycorp.cosmeticwood.common.block.WoodBlock;
import net.smileycorp.cosmeticwood.common.recipe.ShapedWoodRecipe;
import net.smileycorp.cosmeticwood.common.recipe.ShapelessWoodRecipe;
import net.smileycorp.cosmeticwood.common.tile.TileWood;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


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
						if (o!=null && o instanceof Block && o instanceof WoodBlock) {
							blocks.add((Block) o);
						}
					}
					PLUGINS.add(plugin);
					BLOCKS.addAll(blocks);
				} catch (Exception e) {
					CosmeticWood.logError("Error loading plugin " + modid, e);
				}
			} else CosmeticWood.logInfo("Mod " + modid + " not detected. Skipping plugin.");
		}
		for (Block block : BLOCKS) if (block instanceof WoodBlock) {
			Item item = ((WoodBlock)block).getItem();
			ITEMS.add(item);
			Class tile = ((WoodBlock)block).getTile();
			if (!TILE_ENTITIES.contains(tile)) {
				try {
					GameRegistry.registerTileEntity(tile, ((TileWood) tile.newInstance()).getRegistryName());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void registerBlocks(RegistryEvent.Register<Block> event){
		ForgeRegistries.BLOCKS.registerAll(BLOCKS.toArray(new Block[] {}));
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void registerItems(RegistryEvent.Register<Item> event){
		ForgeRegistries.ITEMS.registerAll(ITEMS.toArray(new Item[] {}));
	}
	
	public static void replaceRecipes(){
		IForgeRegistry<IRecipe> recipes = ForgeRegistries.RECIPES;
		CosmeticWood.logInfo("Replacing recipes");
		recipes.forEach(recipe -> {
			if (!(recipe.getRecipeOutput().getItem() instanceof ItemBlockSimpleWood)) return;
			CosmeticWood.logInfo("Replacing recipe " + recipe.getRegistryName());
			recipes.register(recipe instanceof IShapedRecipe ? new ShapedWoodRecipe((IShapedRecipe) recipe) : new ShapelessWoodRecipe(recipe));
		});
	}
	
}
