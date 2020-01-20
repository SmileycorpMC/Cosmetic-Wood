package net.smileycorp.cosmeticwood.common;

import net.minecraft.util.ResourceLocation;

public class ModDefinitions {
	public static final String modid = "cosmeticwood";
	public static final String name = "Cosmetic Wood";
	public static final String version = "alpha 1.1a";
	public static final String location = "net.smileycorp.cosmeticwood.core.";
	public static final String client = location + "client.ClientProxy";
	public static final String common = location + "common.CommonProxy";
	
	public static String getName(String name) {
		return modid + "." + name.replace("_", "");
	}
	
	public static ResourceLocation getResource(String name) {
		return new ResourceLocation(modid, name.toLowerCase());
	}

	public static String getResourcePath(ResourceLocation registryName) {
		return modid + ":blocks/ores/" + name.toLowerCase();
	}

	public static String format(String name) {
		return name.substring(4).replace("planks", "").replace("plank", "").replace("_", "");
	}
}
