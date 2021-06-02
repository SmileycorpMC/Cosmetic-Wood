package net.smileycorp.cosmeticwood.common;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;

public class ModDefinitions {
	public static final String modid = "cosmeticwood";
	public static final String name = "Cosmetic Wood";
	public static final String version = "1.1.0";
	public static final String dependencies = "required-after:atlaslib";
	public static final String location = "net.smileycorp.cosmeticwood.";
	public static final String client = location + "client.ClientProxy";
	public static final String common = location + "common.CommonProxy";
	
	public static String getName(String name) {
		return modid + "." + name.replace("_", "");
	}
	
	public static ResourceLocation getResource(Block block) {
		return getResource(block.getRegistryName().getResourcePath());
	}
	
	public static ResourceLocation getResource(String name) {
		return new ResourceLocation("cosmeticwood", name.toLowerCase());
	}
	
	public static ResourceLocation getRegistry(String name) {
		String[] strs = name.split("[.]");
		if (strs.length>1) return new ResourceLocation(strs[0], strs[1]);
		else return new ResourceLocation(name.toLowerCase());
	}

	public static String format(String name) {
		name = name.toLowerCase();
		name = name.replace(" planks", "").replace(" wood", "").replace(" log", "");
		name = name.replace(" ", "_");
		return name;
	}
}
