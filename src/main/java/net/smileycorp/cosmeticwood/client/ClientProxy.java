package net.smileycorp.cosmeticwood.client;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.IRegistry;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.smileycorp.atlas.api.client.CustomStateMapper;
import net.smileycorp.atlas.api.client.RenderingUtils;
import net.smileycorp.atlas.api.client.TextureAtlasGreyscale;
import net.smileycorp.cosmeticwood.common.CommonProxy;
import net.smileycorp.cosmeticwood.common.Constants;
import net.smileycorp.cosmeticwood.common.ContentRegistry;
import net.smileycorp.cosmeticwood.common.block.WoodBlock;
import net.smileycorp.cosmeticwood.common.item.ItemBlockWood;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

@SideOnly(Side.CLIENT)
@EventBusSubscriber(value=Side.CLIENT, modid = Constants.MODID)
public class ClientProxy extends CommonProxy {

	private static Map<String, TextureAtlasSprite> GREYSCALE_SPRITES = new HashMap<String, TextureAtlasSprite>();

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
	}

	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);
		for (Block block : ContentRegistry.BLOCKS) ((WoodBlock) block).initClient();
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		super.postInit(event);
		FMLClientHandler.instance().refreshResources();
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void stitchTextureEvent(TextureStitchEvent.Pre event) {
		registerFallbackSprite("plank", new ResourceLocation("minecraft", "blocks/planks_oak"));
		registerFallbackSprite("log_top", new ResourceLocation("minecraft", "blocks/log_oak_top"));
		registerFallbackSprite("log_side", new ResourceLocation("minecraft", "blocks/log_oak"));
	}

	public void registerFallbackSprite(String key, ResourceLocation registry) {
		TextureMap map = Minecraft.getMinecraft().getTextureMapBlocks();
		TextureAtlasSprite sprite = new TextureAtlasGreyscale(registry);
		map.setTextureEntry(sprite);
		GREYSCALE_SPRITES.put(key, sprite);
	}

	@SubscribeEvent
	public static void blockColourHandler(ColorHandlerEvent.Block event) {
		BlockColors registry = event.getBlockColors();
		registry.registerBlockColorHandler(new CWBlockColour(), ContentRegistry.BLOCKS.toArray(new Block[]{}));
	}

	@SubscribeEvent
	public static void itemColourHandler(ColorHandlerEvent.Item event) {
		ItemColors registry = event.getItemColors();
		registry.registerItemColorHandler(new CWItemColour(), ContentRegistry.ITEMS.toArray(new Item[]{}));
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void registerModels(ModelRegistryEvent event) {
		for (Block block : ContentRegistry.BLOCKS) {
			Item item = Item.getItemFromBlock(block);
			ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(Constants.loc(block), ((WoodBlock)block).getItemVariant()));
			ModelLoader.setCustomStateMapper(block, new CustomStateMapper(Constants.MODID, block.getRegistryName().getResourcePath()));
			if (((ItemBlockWood)item).getITESR() != null) item.setTileEntityItemStackRenderer(((ItemBlockWood)item).getITESR());
		}
	}

	@SubscribeEvent
	public static void onModelBake(ModelBakeEvent event) {
		IRegistry<ModelResourceLocation, IBakedModel> registry = event.getModelRegistry();
		RenderingUtils.replaceRegisteredModel(new ModelResourceLocation(Constants.loc("wooden_button"), "inventory"), registry, BakedModelCW.class);
		for (Block block : ContentRegistry.BLOCKS) {
			for (IBlockState state : block.getBlockState().getValidStates()) {
				RenderingUtils.replaceRegisteredModel(getModelLocation(state), registry, BakedModelCW.class);
			}
		}
	}

	public static ModelResourceLocation getModelLocation(IBlockState state) {
		String property = "";
		for (Entry<IProperty<?>, Comparable<?>> entry : state.getProperties().entrySet()){
			if (property.length() > 0) property += ",";
			property += entry.getKey().getName();
			property += "=";
			property += entry.getValue().toString();
		}
		if (property.isEmpty()) property = ((WoodBlock) state.getBlock()).getItemVariant();
		return new ModelResourceLocation(Constants.loc(state.getBlock().getRegistryName().getResourcePath()), property);
	}

	public static TextureAtlasSprite getGreyscaleSprite(String key) {
		return GREYSCALE_SPRITES.get(key);
	}
}
