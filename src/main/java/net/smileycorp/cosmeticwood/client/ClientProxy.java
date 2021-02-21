package net.smileycorp.cosmeticwood.client;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.registry.IRegistry;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.smileycorp.atlas.api.client.RenderingUtils;
import net.smileycorp.atlas.api.interfaces.ISidedProxy;
import net.smileycorp.cosmeticwood.common.ModDefinitions;

@EventBusSubscriber(value=Side.CLIENT, modid=ModDefinitions.modid)
public class ClientProxy implements ISidedProxy {
	
	
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		
	}

	@Override
	public void init(FMLInitializationEvent event) {
		
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		
	}
	
	@SubscribeEvent
	public static void onModelBake(ModelBakeEvent event) {
		IRegistry<ModelResourceLocation, IBakedModel> registry = event.getModelRegistry();
		RenderingUtils.replaceRegisteredModel(new ModelResourceLocation(ModDefinitions.getResource("crafting_table"), "normal"), registry, BakedModelCW.class);
	}
}
