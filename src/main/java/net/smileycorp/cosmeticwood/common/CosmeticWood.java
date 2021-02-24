package net.smileycorp.cosmeticwood.common;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.smileycorp.atlas.api.interfaces.ISidedProxy;
import net.smileycorp.cosmeticwood.common.block.CWBlocks;
import net.smileycorp.cosmeticwood.common.tileentity.CWTiles;

@Mod(modid=ModDefinitions.modid, name=ModDefinitions.name, version=ModDefinitions.version, dependencies = "required-after:atlascore")
public class CosmeticWood {
	
	@SidedProxy(clientSide = ModDefinitions.client, serverSide = ModDefinitions.common)
	public static ISidedProxy proxy;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event){
		proxy.preInit(event);
		CWBlocks.registerContent();
		CWTiles.registerContent();
		MinecraftForge.EVENT_BUS.register(proxy);
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event){
		proxy.init(event);
		WoodHandler.buildProperties();
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event){
		proxy.postInit(event);
	}
}
