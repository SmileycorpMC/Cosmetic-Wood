package net.smileycorp.cosmeticwood.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid=ModDefinitions.modid, name=ModDefinitions.name, version=ModDefinitions.version, dependencies = ModDefinitions.dependencies)
public class CosmeticWood {
	
	private static Logger logger = LogManager.getLogger(ModDefinitions.name);
	
	@SidedProxy(clientSide = ModDefinitions.client, serverSide = ModDefinitions.common)
	public static CommonProxy proxy;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event){
		proxy.preInit(event);
		MinecraftForge.EVENT_BUS.register(proxy);
		ContentRegistry.preInit();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event){
		proxy.init(event);
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event){
		proxy.postInit(event);
	}
	
	public static void logInfo(Object message) {
		logger.info(message);
	}
	
	public static void logError(Object message, Exception e) {
		logger.error(message);
		e.printStackTrace();
	}

}
