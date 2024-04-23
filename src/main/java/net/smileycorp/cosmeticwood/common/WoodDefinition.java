package net.smileycorp.cosmeticwood.common;

import com.google.common.collect.ImmutableMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.smileycorp.cosmeticwood.client.ClientWoodDefinition;

import java.awt.*;

public class WoodDefinition {
	
	private final ResourceLocation registry;
	private final ItemStack plank;
	private final ItemStack log;
	
	private ClientWoodDefinition clientside;
	
	public WoodDefinition(ResourceLocation registry, ItemStack plank, ItemStack log) {
		this.registry = registry;
		this.plank = plank;
		this.log = log;
	}
	
	public void initClient() {
		clientside = new ClientWoodDefinition(plank, log);
	}
	
	public String getModid() {
		return registry.getResourceDomain();
	}
	
	public String getName() {
		return registry.getResourcePath();
	}
	
	public ResourceLocation getRegistry() {
		return registry;
	}
	
	public ItemStack getPlankStack() {
		return plank;
	}
	
	public ItemStack getLogStack() {
		return log;
	}

	public ImmutableMap<String, String> getTextures() {
		return clientside == null ? null : clientside.getTextures();
	}
	
	public Color getColour() {
		return clientside == null ? null : clientside.getColour();
	}

}
