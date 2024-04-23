package net.smileycorp.cosmeticwood.common;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.logging.Level;
import java.util.logging.Logger;

@Mod(modid = ModDefinitions.modid, name = ModDefinitions.name, version = ModDefinitions.version, dependencies = ModDefinitions.dependencies)
public class CosmeticWood {

	private static final Logger logger = Logger.getLogger(ModDefinitions.name);

	@SidedProxy(clientSide = ModDefinitions.client, serverSide = ModDefinitions.common)
	public static CommonProxy proxy;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event){
		proxy.preInit(event);
		MinecraftForge.EVENT_BUS.register(proxy);
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
		logger.info(String.valueOf(message));
	}

	public static void logError(Object message, Exception e) {
		logger.log(Level.SEVERE, String.valueOf(message));
		e.printStackTrace();
	}

}
