package net.smileycorp.cosmeticwood.client;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.smileycorp.atlas.api.client.TextureAtlasGreyscale;
import net.smileycorp.atlas.api.util.TextUtils;
import net.smileycorp.cosmeticwood.common.CommonProxy;
import net.smileycorp.cosmeticwood.common.Constants;
import net.smileycorp.cosmeticwood.common.data.WoodHandler;
import net.smileycorp.cosmeticwood.common.data.WoodTypeStorage;
import net.smileycorp.cosmeticwood.common.registry.ContentRegistry;
import net.smileycorp.cosmeticwood.common.registry.item.WoodStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@SideOnly(Side.CLIENT)
@EventBusSubscriber(value=Side.CLIENT, modid = Constants.MODID)
public class ClientProxy extends CommonProxy {

	private static Map<String, TextureAtlasSprite> GREYSCALE_SPRITES = new HashMap<String, TextureAtlasSprite>();
    
    @Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
		ModelLoaderRegistry.registerLoader(new CWModelLoader());
	}

	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		super.postInit(event);
		Minecraft mc = Minecraft.getMinecraft();
		ItemModelMesher mesher = mc.getRenderItem().getItemModelMesher();
		BlockModelShapes blockModels = mc.getBlockRendererDispatcher().getBlockModelShapes();
		ItemColors itemColours = mc.getItemColors();
		BlockColors blockColors = mc.getBlockColors();
		for (Block block : ContentRegistry.BLOCKS) {
			Item item = Item.getItemFromBlock(block);
			ModelResourceLocation loc = new ModelResourceLocation(block.getRegistryName() + ".wooditem", "inventory");
			ModelLoader.setCustomModelResourceLocation(item, 0, loc);
			StateMap mapper = new StateMap.Builder().withSuffix(".woodblock").build();
			ModelLoader.setCustomStateMapper(block, mapper);
			blockModels.registerBlockWithStateMapper(block, mapper);
			mesher.register(item, 0, loc);
			itemColours.registerItemColorHandler(new CWItemColour(), block);
			blockColors.registerBlockColorHandler(new CWBlockColour(), block);
		}
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
	public static void addTooltip(ItemTooltipEvent event) {
		ItemStack stack = event.getItemStack();
		if (stack == null) return;
		if (!((WoodStack)(Object)stack).isWoodItem()) return;
		NBTTagCompound nbt = stack.getTagCompound();
		List<String> tooltip = event.getToolTip();
		if (nbt != null && nbt.hasKey("type")) {
			String type = WoodHandler.getInstance().fixData(nbt.getString("type")).getResourcePath();
			tooltip.add(TextUtils.toProperCase(type));
		}
		else tooltip.add(TextUtils.toProperCase(WoodHandler.getDefault().getResourcePath()));
	}

	public static ModelResourceLocation getModelLocation(IBlockState state) {
		String property = "";
		for (Entry<IProperty<?>, Comparable<?>> entry : state.getProperties().entrySet()){
			if (property.length() > 0) property += ",";
			property += entry.getKey().getName();
			property += "=";
			property += entry.getValue().toString();
		}
		if (property.isEmpty()) property = "inventory";
		return new ModelResourceLocation(Constants.loc(state.getBlock().getRegistryName().getResourcePath()), property);
	}

	public static TextureAtlasSprite getGreyscaleSprite(String key) {
		return GREYSCALE_SPRITES.get(key);
	}
	
	public static void syncChunk(int x, int z, NBTTagCompound nbt) {
		Chunk chunk = Minecraft.getMinecraft().world.getChunkFromChunkCoords(x, z);
		if (chunk == null) return;
		if (!chunk.hasCapability(WoodTypeStorage.CAPABILITY, null)) return;
		chunk.getCapability(WoodTypeStorage.CAPABILITY, null).load(nbt);
	}
	
}
