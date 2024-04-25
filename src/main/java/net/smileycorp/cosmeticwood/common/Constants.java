package net.smileycorp.cosmeticwood.common;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class Constants {
	public static final String MODID = "cosmeticwood";
	public static final String NAME = "Cosmetic Wood";
	public static final String VERSION = "1.1.2";
	public static final String DEPENDENCIES = "required-after:atlaslib";
	private static final String PACKAGE = "net.smileycorp.cosmeticwood";
	public static final String CLIENT_PROXY = PACKAGE + ".client.ClientProxy";
	public static final String COMMON_PROXY = PACKAGE + ".common.CommonProxy";
	
	public static String name(String name) {
		return MODID + "." + name.replace("_", "");
	}
	
	public static ResourceLocation loc(Block block) {
		return loc(block.getRegistryName().getResourcePath());
	}
	
	public static ResourceLocation loc(String name) {
		return new ResourceLocation(MODID, name.toLowerCase());
	}
	
	public static ResourceLocation getRegistry(String name) {
		String[] strs = name.split("[.]");
		if (strs.length>1) return new ResourceLocation(strs[0], strs[1]);
		else return new ResourceLocation(name.toLowerCase());
	}

	public static ResourceLocation format(ItemStack stack) {
		ItemBlock item = (ItemBlock) stack.getItem();
		String name = item.getItemStackDisplayName(stack);
		name = name.toLowerCase();
		name = name.replace(" planks", "").replace(" wood", "").replace(" log", "");
		name = name.replace(" ", "_");
		return new ResourceLocation(item.getRegistryName().getResourceDomain(), name);
	}
}

