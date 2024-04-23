package net.smileycorp.cosmeticwood.common;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;
import net.smileycorp.atlas.api.util.RecipeUtils;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.Map.Entry;

public class WoodHandler {
	
	private static Map<ResourceLocation, WoodDefinition> WOOD_MAP = new HashMap<>();
	private static boolean clientInitialized;
	
	static {
		WOOD_MAP.put(getDefault(), new WoodDefinition(getDefault(), new ItemStack(Blocks.PLANKS), new ItemStack(Blocks.LOG)));
	}
	
	public static void buildProperties() {
		Map<ResourceLocation, ItemStack> planks = new HashMap<>();
		Map<ResourceLocation, ItemStack> logs = new HashMap<>();
		for (String ore:OreDictionary.getOreNames()) {
			if (ore.contains("plankWood")) for (ItemStack oreStack : OreDictionary.getOres(ore)) {
				if (oreStack.getItem() instanceof ItemBlock) {
				NonNullList<ItemStack> subBlocks = NonNullList.create();
					if(oreStack.getMetadata() == OreDictionary.WILDCARD_VALUE)
						((ItemBlock) oreStack.getItem()).getBlock().getSubBlocks(null, subBlocks);
					else subBlocks.add(oreStack);
					for (ItemStack stack : subBlocks) {
						ResourceLocation name = ModDefinitions.format(stack);
						if (!planks.containsKey(name)) planks.put(name, stack);
					}
				}
			}
			if (ore.contains("logWood")) for (ItemStack oreStack : OreDictionary.getOres(ore)) {
				if (oreStack.getItem() instanceof ItemBlock) {
				NonNullList<ItemStack> subBlocks = NonNullList.create();
					if(oreStack.getMetadata() == OreDictionary.WILDCARD_VALUE) {
						((ItemBlock) oreStack.getItem()).getBlock().getSubBlocks(null, subBlocks);
					} else subBlocks.add(oreStack);
					for (ItemStack stack : subBlocks) {
						ResourceLocation name = ModDefinitions.format(stack);
						if (!logs.containsKey(name)) logs.put(name, stack);
					}
				}
			}
		}
		for(Entry<ResourceLocation, ItemStack> entry : planks.entrySet()) {
			ResourceLocation key = entry.getKey();
			ItemStack log = null;
			if (logs.containsKey(key)) log = logs.get(key);
			WOOD_MAP.put(entry.getKey(), new WoodDefinition(key, entry.getValue(), log));
		}
		CosmeticWood.logInfo("detected wood types " + WOOD_MAP.keySet());
		
	}
	
	public static boolean contains(String key) {
		return contains(fixData(key));
	}
	
	public static boolean contains(ResourceLocation key) {
		return WOOD_MAP.containsKey(key);
	}
	
	public static Set<ResourceLocation> getTypes(String... modids) {
		Set<ResourceLocation> result = new HashSet(WOOD_MAP.keySet());
		if (modids.length > 0) {
			List<String> mods = Lists.newArrayList(modids);
			for (ResourceLocation registry : WOOD_MAP.keySet())
				if (mods.contains(registry.getResourceDomain())) result.remove(registry);
		}
		return result;
	}
	
	public static ItemStack getPlankStack(ResourceLocation name) {
		return WOOD_MAP.containsKey(name) ? WOOD_MAP.get(name).getPlankStack()
				: new ItemStack(Blocks.PLANKS);
	}
	
	public static ItemStack getLogStack(ResourceLocation name) {
		if (WOOD_MAP.containsKey(name)){
			ItemStack stack = WOOD_MAP.get(name).getLogStack();
			if (stack != null ) return stack ;
		}
		return new ItemStack(Blocks.LOG, 1, OreDictionary.WILDCARD_VALUE);
	}
	
	public static ResourceLocation getDefault() {
		return new ResourceLocation("oak");
	}
	
	public static String getName(ItemStack stack) {
		for (WoodDefinition wood : WOOD_MAP.values())
			if (RecipeUtils.compareItemStacks(stack, wood.getLogStack(), true)
					|| RecipeUtils.compareItemStacks(stack, wood.getPlankStack(), true))
				return wood.getName();
		return null;
	}
	
	public static ResourceLocation getRegistry(ItemStack stack) {
		for (WoodDefinition wood : WOOD_MAP.values())
			if (RecipeUtils.compareItemStacks(stack, wood.getLogStack(), true)
					|| RecipeUtils.compareItemStacks(stack, wood.getPlankStack(), true))
				return wood.getRegistry();
		return null;
	}

	public static ResourceLocation fixData(String name) {
		if (name == null) return getDefault();
		if (name.contains(":")) return new ResourceLocation(name);
		for (ResourceLocation registry : getTypes())
			if (registry.getResourcePath().equals(name)) return registry;
		return new ResourceLocation(name);
	}

	public static void fixData(ItemStack stack) {
		if (ContentRegistry.ITEMS.contains(stack.getItem())) {
			NBTTagCompound nbt = stack.getTagCompound();
			if (nbt != null && nbt.hasKey("type")) {
				String type = nbt.getString("type");
				if (!type.contains(":")) nbt.setString("type", fixData(type).toString());
			}
		}
	}
	
	public static ImmutableMap<String, String> getTextures(ResourceLocation name) {
		if (!clientInitialized) initClient();
		return WOOD_MAP.containsKey(name) ? WOOD_MAP.get(name).getTextures() : WOOD_MAP.get(getDefault()).getTextures()  ;
	}
	
	public static Color getColour(ResourceLocation name) {
		if (!clientInitialized) initClient();
		return WOOD_MAP.containsKey(name) ? WOOD_MAP.get(name).getColour() : WOOD_MAP.get(getDefault()).getColour();
	}
	
	private static void initClient() {
		WOOD_MAP.values().forEach(WoodDefinition::initClient);
		clientInitialized = true;
	}
	
}
