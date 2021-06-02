package net.smileycorp.cosmeticwood.common;

import java.awt.Color;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.smileycorp.cosmeticwood.client.ClientWoodDefinition;

import com.google.common.collect.ImmutableMap;

public class WoodDefinition {
	
	private final String name;
	private final ItemStack plank;
	private final ItemStack log;
	
	private ClientWoodDefinition clientside;
	
	public WoodDefinition(String name, ItemStack plank, ItemStack log) {
		this.name=name;
		this.plank=plank;
		this.log=log;
		if (FMLCommonHandler.instance().getSide()==Side.CLIENT) {
			clientside = new ClientWoodDefinition(plank, log);
		}
	}
	
	public String getName() {
		return name;
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
