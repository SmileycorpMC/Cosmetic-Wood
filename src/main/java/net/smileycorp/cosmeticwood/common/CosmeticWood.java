package net.smileycorp.cosmeticwood.common;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.smileycorp.cosmeticwood.common.block.CWBlocks;

@Mod(modid=ModDefinitions.modid, name=ModDefinitions.name, version=ModDefinitions.version, dependencies = "required-after:atlascore")
public class CosmeticWood {
	@EventHandler
	public void preInit(FMLPreInitializationEvent event){
		CWBlocks.registerContent();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event){
		EnumBuilder.buildProperties();
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event){
		
	}
}
