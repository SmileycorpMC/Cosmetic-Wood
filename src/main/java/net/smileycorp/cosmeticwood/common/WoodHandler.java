package net.smileycorp.cosmeticwood.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.smileycorp.atlas.api.block.PropertyString;
import net.smileycorp.cosmeticwood.common.block.CWBlocks;

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
								addLogType(name, stack);
							}
						}
					}
				}
			}
		}
		System.out.println("[cosmeticwood] detected wood types "+PLANK_MAP.keySet());
		for(String type : getTypes()) {
			ItemStack plank = getPlankStack(type).copy();
			ItemStack log = getLogStack(type).copy();
			//addRecipe(ModDefinitions.getResource("crafting_table"), type, new ItemStack(CWBlocks.WORKBENCH, 1), plank, log, new Object[]{"PP", "PP"});
			addRecipe(ModDefinitions.getResource("crafting_table"), type, new ItemStack(CWBlocks.WORKBENCH), new Object[]{"PP", "PP", 'P', plank});
		}
		
	}
	
	public static void addPlankType(String name, ItemStack stack) {
		PLANK_MAP.put(name, stack);
	}
	
	public static void addLogType(String name, ItemStack stack) {
		LOG_MAP.put(name, stack);
	}
	
	public static List<String> getTypes() {
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
	
	private static void addRecipe(ResourceLocation base, String type, ItemStack output, Object... pattern) {
		ResourceLocation registry = ModDefinitions.getResource(base.getResourcePath()+"_"+type);
		NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("type", type);
        output.setTagCompound(nbt);
		GameRegistry.addShapedRecipe(registry, base, output, pattern);
	}

}
