package net.smileycorp.cosmeticwood.common;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;
import net.smileycorp.atlas.api.util.RecipeUtils;

import com.google.common.collect.ImmutableMap;

public class WoodHandler {
	
	private static Map<String, WoodDefinition> WOOD_MAP = new HashMap<String, WoodDefinition>();
	
	static {
		WOOD_MAP.put(getDefault(), new WoodDefinition(getDefault(), new ItemStack(Blocks.PLANKS), new ItemStack(Blocks.LOG)));
	}
	
	public static void buildProperties() {
		Map<String, ItemStack> planks = new HashMap<String, ItemStack>();
		Map<String, ItemStack> logs = new HashMap<String, ItemStack>();
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
							if (!planks.containsKey(name)) {
								planks.put(name, stack);
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
							if (!logs.containsKey(name)) {
								logs.put(name, stack);
							}
						}
					}
				}
			}
		}
		for(Entry<String, ItemStack> entry : planks.entrySet()) {
			String name = entry.getKey();
			ItemStack log = null;
			if (logs.containsKey(name)) {
				log = logs.get(name);
			}
			WOOD_MAP.put(entry.getKey(), new WoodDefinition(name, entry.getValue(), log));
		}
		
		System.out.println("[cosmeticwood] detected wood types " + WOOD_MAP.keySet());
		
	}
	
	public static List<String> getTypes() {
		return new ArrayList<String>(WOOD_MAP.keySet());
	}
	
	public static ItemStack getPlankStack(String name) {
		if (WOOD_MAP.containsKey(name)){
			return WOOD_MAP.get(name).getPlankStack();
		}
		return new ItemStack(Blocks.PLANKS);
	}
	
	public static ItemStack getLogStack(String name) {
		if (WOOD_MAP.containsKey(name)){
			ItemStack stack = WOOD_MAP.get(name).getLogStack();
			if (stack != null ) return stack ;
		}
		return new ItemStack(Blocks.LOG, 1, OreDictionary.WILDCARD_VALUE);
	}
	
	public static String getDefault() {
		return "oak";
	}
	
	public static ImmutableMap<String, String> getTextures(String name) {
		return WOOD_MAP.containsKey(name) ? WOOD_MAP.get(name).getTextures() : WOOD_MAP.get(getDefault()).getTextures()  ;
	}
	
	public static Color getColour(String name) {
		return WOOD_MAP.containsKey(name) ? WOOD_MAP.get(name).getColour() : WOOD_MAP.get("oak").getColour();
	}

	public static String getName(ItemStack stack) {
		for (WoodDefinition wood : WOOD_MAP.values()) {
			if (RecipeUtils.compareItemStacks(stack, wood.getLogStack(), true)||RecipeUtils.compareItemStacks(stack, wood.getPlankStack(), true)) {
				return wood.getName();
			}
		}
		return null;
	}

}
