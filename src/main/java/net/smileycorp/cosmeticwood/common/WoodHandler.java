package net.smileycorp.cosmeticwood.common;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.ArrayUtils;

import com.google.common.collect.ImmutableMap;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import net.smileycorp.atlas.api.block.PropertyString;
import net.smileycorp.atlas.api.client.TextureAtlasGreyscale;
import net.smileycorp.cosmeticwood.common.block.CWBlocks;

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
			ItemStack log = new ItemStack(Blocks.LOG);
			if (logs.containsKey(name)) {
				log = logs.get(name);
			}
			WOOD_MAP.put(entry.getKey(), new WoodDefinition(name, entry.getValue(), log));
		}
		
		System.out.println("[cosmeticwood] detected wood types " + WOOD_MAP.keySet());
		for(String type : getTypes()) {
			ItemStack plank = getPlankStack(type).copy();
			ItemStack log = getLogStack(type).copy();
			addRecipe(new ResourceLocation("minecraft", "crafting_table"), type, new ItemStack(CWBlocks.WORKBENCH), new Object[]{"PP", "PP", 'P', plank});
			addRecipe(new ResourceLocation("minecraft", "wooden_pressure_plate"), type, new ItemStack(CWBlocks.PRESSURE_PLATE), new Object[]{"PP", 'P', plank});
		}
		
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
			return WOOD_MAP.get(name).getLogStack();
		}
		return new ItemStack(Blocks.LOG);
	}
	
	public static String getDefault() {
		return "oak";
	}
	
	@SideOnly(Side.CLIENT)
	public static ImmutableMap<String, String> getTextures(String name) {
		return WOOD_MAP.get(name).getTextures();
	}
	
	@SideOnly(Side.CLIENT)
	public static Color getColour(String name) {
		System.out.print(name);
		return WOOD_MAP.containsKey(name) ? WOOD_MAP.get(name).getColour() : WOOD_MAP.get("oak").getColour();
	}
	
	private static void addRecipe(ResourceLocation base, String type, ItemStack output, Object... pattern) {
		ResourceLocation registry = new ResourceLocation(base.getResourceDomain(), base.getResourcePath()+"_"+type);
		NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("type", type);
        output.setTagCompound(nbt);
		GameRegistry.addShapedRecipe(registry, base, output, pattern);
	}

}
