package net.smileycorp.cosmeticwood.client;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.registry.IRegistry;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.smileycorp.atlas.api.util.TextUtils;
import net.smileycorp.cosmeticwood.api.registry.block.WoodBlock;
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
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void renderText(RenderGameOverlayEvent.Text event) {
		Minecraft mc = Minecraft.getMinecraft();
		if (!mc.gameSettings.showDebugInfo) return;
		RayTraceResult result = mc.objectMouseOver;
		if (result == null) return;
		if (result.typeOfHit != RayTraceResult.Type.BLOCK) return;
		BlockPos pos = result.getBlockPos();
		if (pos == null) return;
		WorldClient world = mc.world;
		IBlockState state = world.getBlockState(pos);
		if (!((WoodBlock) state).isWood()) return;
		ResourceLocation type = WoodTypeStorage.getWoodType(world, pos);
		event.getRight().add("wood_type: " + type);
	}
	
	public static void syncChunk(int x, int z, NBTTagCompound nbt) {
		Chunk chunk = Minecraft.getMinecraft().world.getChunkFromChunkCoords(x, z);
		if (chunk == null) return;
		if (!chunk.hasCapability(WoodTypeStorage.CAPABILITY, null)) return;
		chunk.getCapability(WoodTypeStorage.CAPABILITY, null).load(nbt);
	}
	
}
