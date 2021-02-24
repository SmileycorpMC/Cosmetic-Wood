package net.smileycorp.cosmeticwood.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.IRegistry;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.smileycorp.atlas.api.client.RenderingUtils;
import net.smileycorp.atlas.api.client.TextureAtlasGreyscale;
import net.smileycorp.atlas.api.interfaces.ISidedProxy;
import net.smileycorp.cosmeticwood.common.ModDefinitions;
import net.smileycorp.cosmeticwood.common.WoodHandler;
import net.smileycorp.cosmeticwood.common.block.CWBlocks;
import net.smileycorp.cosmeticwood.common.tileentity.TileEntitySimpleWood;

@SideOnly(Side.CLIENT)
@EventBusSubscriber(value=Side.CLIENT, modid = ModDefinitions.modid)
public class ClientProxy implements ISidedProxy {
	
	public static TextureAtlasSprite GREYSCALE_PLANKS;;
	
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		
	}

	@Override
	public void init(FMLInitializationEvent event) {
		
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		FMLClientHandler.instance().refreshResources();
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void stitchTextureEvent(TextureStitchEvent.Pre event) {
		TextureMap map = Minecraft.getMinecraft().getTextureMapBlocks();
		GREYSCALE_PLANKS = new TextureAtlasGreyscale(new ResourceLocation("minecraft", "blocks/planks_oak"));
		map.setTextureEntry(GREYSCALE_PLANKS);
	}
	
	@SubscribeEvent
	public static void blockColourHandler(ColorHandlerEvent.Block event) {
		BlockColors registry = event.getBlockColors();
		registry.registerBlockColorHandler(new CWParticleColour(), CWBlocks.blocks);
	}
	
	@SubscribeEvent
	public static void onModelBake(ModelBakeEvent event) {
		IRegistry<ModelResourceLocation, IBakedModel> registry = event.getModelRegistry();
		RenderingUtils.replaceRegisteredModel(new ModelResourceLocation(ModDefinitions.getResource("crafting_table"), "normal"), registry, BakedModelCW.class);
		RenderingUtils.replaceRegisteredModel(new ModelResourceLocation(ModDefinitions.getResource("wooden_pressure_plate"), "powered=false"), registry, BakedModelCW.class);
		RenderingUtils.replaceRegisteredModel(new ModelResourceLocation(ModDefinitions.getResource("wooden_pressure_plate"), "powered=true"), registry, BakedModelCW.class);
	}
}
