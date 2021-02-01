package net.smileycorp.cosmeticwood.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.oredict.OreDictionary;
import net.smileycorp.atlas.api.block.PropertyString;

public class WoodHandler {
	
	private static Map<String, ItemStack> PLANK_MAP = new HashMap<String, ItemStack>();
	private static Map<String, ItemStack> LOG_MAP = new HashMap<String, ItemStack>();
	
	static{
		addPlankType(getDefault(), new ItemStack(Blocks.PLANKS, 1, 0));
		addLogType(getDefault(), new ItemStack(Blocks.LOG, 1, 0));
	}
	
	public static void buildProperties() {
		for (String ore:OreDictionary.getOreNames()) {
			if (ore.contains("plankWood")) {
				for (ItemStack oreStack : OreDictionary.getOres(ore)) {
					if (oreStack.getItem() instanceof ItemBlock) {
					NonNullList<ItemStack> subBlocks = NonNullList.create();
						if(oreStack.getMetadata() == OreDictionary.WILDCARD_VALUE) {
							((ItemBlock) oreStack.getItem()).getBlock().getSubBlocks(null, subBlocks);
						} else {
							subBlocks.add(oreStack);
						}
						for (ItemStack stack : subBlocks) {
							String name = ModDefinitions.format(((ItemBlock) stack.getItem()).getItemStackDisplayName(stack));
							if (!PLANK_MAP.containsKey(name)) {
								System.out.println("adding variant " + name);
								addPlankType(name, stack);
							}
						}
					}
				}
			}
			if (ore.contains("logWood")) {
				for (ItemStack oreStack : OreDictionary.getOres(ore)) {
					if (oreStack.getItem() instanceof ItemBlock) {
					NonNullList<ItemStack> subBlocks = NonNullList.create();
						if(oreStack.getMetadata() == OreDictionary.WILDCARD_VALUE) {
							((ItemBlock) oreStack.getItem()).getBlock().getSubBlocks(null, subBlocks);
						} else {
							subBlocks.add(oreStack);
						}
						for (ItemStack stack : subBlocks) {
							String name = ModDefinitions.format(((ItemBlock) stack.getItem()).getItemStackDisplayName(stack));
							if (!LOG_MAP.containsKey(name)) {
								System.out.println("adding variant " + name);
								addLogType(name, stack);
							}
						}
					}
				}
			}
		}
	}
	
	public static void addPlankType(String name, ItemStack stack) {
		PLANK_MAP.put(name, stack);
	}
	
	public static void addLogType(String name, ItemStack stack) {
		LOG_MAP.put(name, stack);
	}
	
	public static List<String> getTypes() {
		System.out.println(PLANK_MAP.keySet());
		return new ArrayList<String>(PLANK_MAP.keySet());
	}
	
	public static ItemStack getPlankStack(String name) {
		if (PLANK_MAP.containsKey(name)){
			return PLANK_MAP.get(name);
		}
		return new ItemStack(Blocks.PLANKS, 1, 0);
	}
	
	public static ItemStack getLogStack(String name) {
		if (LOG_MAP.containsKey(name)){
			return LOG_MAP.get(name);
		}
		return new ItemStack(Blocks.PLANKS, 1, 0);
	}
	
	public static String getDefault() {
		return "oak";
	}

}
