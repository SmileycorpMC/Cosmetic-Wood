package net.smileycorp.cosmeticwood.common.data;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.oredict.OreDictionary;
import net.smileycorp.cosmeticwood.api.registry.block.WoodBlock;
import net.smileycorp.cosmeticwood.api.registry.item.WoodStack;
import net.smileycorp.cosmeticwood.common.CWLogger;
import net.smileycorp.cosmeticwood.common.Constants;
import net.smileycorp.cosmeticwood.common.registry.ContentRegistry;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class WoodHandler {
	
	private static WoodHandler INSTANCE;
	
	public static Tuple<BlockPos, ResourceLocation> TYPE_CACHE = null;
	
	private Map<ResourceLocation, WoodDefinition> WOOD_MAP = Maps.newLinkedHashMap();
	private boolean clientInitialized;
	
	private WoodHandler() {
		WOOD_MAP.put(getDefault(), new WoodDefinition(getDefault(), new ItemStack(Blocks.PLANKS), new ItemStack(Blocks.LOG)));
	}
	
	public static WoodHandler getInstance() {
		if (INSTANCE == null) INSTANCE = new WoodHandler();
		return INSTANCE;
	}
	
	public static ResourceLocation getDefault() {
		return new ResourceLocation("oak");
	}
	
	public void buildProperties() {
		Map<ResourceLocation, ItemStack> planks = new HashMap<>();
		Map<ResourceLocation, ItemStack> logs = new HashMap<>();
		for (String ore : OreDictionary.getOreNames()) {
			if (ore.contains("plankWood")) for (ItemStack oreStack : OreDictionary.getOres(ore)) {
				if (oreStack.getItem() instanceof ItemBlock) {
				NonNullList<ItemStack> subBlocks = NonNullList.create();
					if(oreStack.getMetadata() == OreDictionary.WILDCARD_VALUE)
						oreStack.getItem().getSubItems(oreStack.getItem().getCreativeTab(), subBlocks);
					else subBlocks.add(oreStack);
					for (ItemStack stack : subBlocks) {
						ResourceLocation name = Constants.format(stack);
						if (!planks.containsKey(name)) planks.put(name, stack);
					}
				}
			}
			if (ore.contains("logWood")) for (ItemStack oreStack : OreDictionary.getOres(ore)) {
				if (oreStack.getItem() instanceof ItemBlock) {
				NonNullList<ItemStack> subBlocks = NonNullList.create();
					if(oreStack.getMetadata() == OreDictionary.WILDCARD_VALUE) {
						oreStack.getItem().getSubItems(oreStack.getItem().getCreativeTab(), subBlocks);
					} else subBlocks.add(oreStack);
					for (ItemStack stack : subBlocks) {
						ResourceLocation name = Constants.format(stack);
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
		CWLogger.logInfo("Detected wood types " + WOOD_MAP.keySet());
	}
	
	public boolean contains(String key) {
		return contains(fixData(key));
	}
	
	public boolean contains(ResourceLocation key) {
		return WOOD_MAP.containsKey(key);
	}
	
	public WoodDefinition get(ResourceLocation name) {
		return WOOD_MAP.containsKey(name) ? WOOD_MAP.get(name) : WOOD_MAP.get(getDefault());
	}
	
	public List<ResourceLocation> getTypes(ResourceLocation defaultType, String... modids) {
		List<ResourceLocation> result = Lists.newArrayList();
		WOOD_MAP.values().forEach(entry -> {
			if (entry == null) return;
			if (!entry.isBaseType()) return;
			if (!entry.getRegistry().equals(defaultType)) for (String modid : modids) if (modid.equals(entry.getModid())) return;
			result.add(entry.getRegistry());});
		return result;
	}
	
	public List<WoodDefinition> getDefinitions() {
		return getDefinitions(null);
	}
	
	public List<WoodDefinition> getDefinitions(@Nullable ResourceLocation defaultType, String... modids) {
		List<WoodDefinition> result = Lists.newArrayList();
		WOOD_MAP.values().forEach(entry -> {
			if (entry == null) return;
			if (!entry.getRegistry().equals(defaultType)) for (String modid : modids) if (modid.equals(entry.getModid())) return;
			result.add(entry);});
		return result;
	}
	
	public ItemStack getPlankStack(ResourceLocation name) {
		return WOOD_MAP.containsKey(name) ? WOOD_MAP.get(name).getPlankStack()
				: new ItemStack(Blocks.PLANKS);
	}
	
	public ItemStack getLogStack(ResourceLocation name) {
		if (WOOD_MAP.containsKey(name)){
			ItemStack stack = WOOD_MAP.get(name).getLogStack();
			if (stack != null ) return stack ;
		}
		return new ItemStack(Blocks.LOG, 1, OreDictionary.WILDCARD_VALUE);
	}
	
	public ResourceLocation getDefault(ItemStack result) {
		return ((WoodBlock)((ItemBlock)result.getItem()).getBlock()).getDefaultType();
	}
	
	public boolean isValidType(ResourceLocation type) {
		if (type == null) return false;
		return WOOD_MAP.containsKey(type);
	}
	
	public ResourceLocation getRegistry(ItemStack stack) {
		if (((WoodStack)(Object)stack).isWood()) return ((WoodStack)(Object)stack).getType(stack);
		for (WoodDefinition wood : WOOD_MAP.values()) if (isStack(stack, wood.getLogStack()) || isStack(stack, wood.getPlankStack())) return wood.getRegistry();
		return null;
	}
	
	private boolean isStack(ItemStack stack1, ItemStack stack2) {
		if (stack1 == null || stack2 == null) return false;
		if (stack1.isEmpty() || stack2.isEmpty()) return false;
		if (stack1.getItem() != stack2.getItem()) return false;
		if (stack1.getMetadata() != stack2.getMetadata() && stack2.getMetadata() != OreDictionary.WILDCARD_VALUE) return false;
		if (stack1.hasTagCompound() ^ stack2.hasTagCompound()) return false;
		return (NBTUtil.areNBTEquals(stack1.getTagCompound(), stack2.getTagCompound(), true));
	}
	
	public ResourceLocation fixData(String name) {
		if (name == null) return getDefault();
		if (name.contains(":")) return new ResourceLocation(name);
		for (ResourceLocation registry : getTypes(null))
			if (registry.getResourcePath().equals(name)) return registry;
		return new ResourceLocation(name);
	}

	public void fixData(ItemStack stack) {
		if (ContentRegistry.ITEMS.contains(stack.getItem())) {
			NBTTagCompound nbt = stack.getTagCompound();
			if (nbt != null && nbt.hasKey("type")) {
				String type = nbt.getString("type");
				if (!type.contains(":")) nbt.setString("type", fixData(type).toString());
			}
		}
	}
	
	public ImmutableMap<String, String> getTextures(ResourceLocation name) {
		if (!clientInitialized) initClient();
		return get(name).getTextures()  ;
	}
	
	public Color getColour(ResourceLocation name) {
		if (!clientInitialized) initClient();
		return get(name).getColour();
	}
	
	private void initClient() {
		WOOD_MAP.values().forEach(WoodDefinition::initClient);
		clientInitialized = true;
	}
	
}
