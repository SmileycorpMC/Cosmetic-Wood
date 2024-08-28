package net.smileycorp.cosmeticwood.client;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.registry.IRegistry;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.smileycorp.atlas.api.util.TextUtils;
import net.smileycorp.cosmeticwood.api.registry.item.WoodStack;
import net.smileycorp.cosmeticwood.common.CommonProxy;
import net.smileycorp.cosmeticwood.common.Constants;
import net.smileycorp.cosmeticwood.common.data.WoodHandler;
import net.smileycorp.cosmeticwood.common.data.WoodTypeStorage;
import net.smileycorp.cosmeticwood.common.registry.ContentRegistry;

import java.util.List;

@SideOnly(Side.CLIENT)
@EventBusSubscriber(value=Side.CLIENT, modid = Constants.MODID)
public class ClientProxy extends CommonProxy {
	
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
		//ModelLoaderRegistry.registerLoader(new CWModelLoader());
	}
	
	/*@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event) {
		for (Block block : ContentRegistry.BLOCKS) ModelLoader.setCustomStateMapper(block, new StateMap.Builder().withSuffix(".woodblock").build());
		for (Item item : ContentRegistry.ITEMS) {
			ModelLoader.setCustomModelResourceLocation(item, 0,
					new ModelResourceLocation(item.getRegistryName() + ".wooditem", "inventory"));
			try {
				ModelLoaderRegistry.getModel(new ModelResourceLocation(Constants.loc(item.getRegistryName().getResourcePath()), "inventory"));
			} catch (Exception e) {}
		}
	}*/
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void bakeModels(ModelBakeEvent event) {
		IRegistry<ModelResourceLocation, IBakedModel> registry = event.getModelRegistry();
		CWModelLoader.bakeModels(registry);
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void stitchTextureEvent(TextureStitchEvent.Pre event) {
		CWModelLoader.stitchTextures(event.getMap());
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
		if (!((WoodStack)(Object)stack).isWood()) return;
		NBTTagCompound nbt = stack.getTagCompound();
		List<String> tooltip = event.getToolTip();
		if (nbt != null && nbt.hasKey("type")) {
			String type = WoodHandler.getInstance().fixData(nbt.getString("type")).getResourcePath();
			tooltip.add(TextUtils.toProperCase(type));
		}
		else tooltip.add(TextUtils.toProperCase(WoodHandler.getDefault().getResourcePath()));
	}
	
	public static void syncChunk(int x, int z, NBTTagCompound nbt) {
		Chunk chunk = Minecraft.getMinecraft().world.getChunkFromChunkCoords(x, z);
		if (chunk == null) return;
		if (!chunk.hasCapability(WoodTypeStorage.CAPABILITY, null)) return;
		chunk.getCapability(WoodTypeStorage.CAPABILITY, null).load(nbt);
	}
	
}
