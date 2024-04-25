package net.smileycorp.cosmeticwood.common.tile;

import net.minecraft.util.ResourceLocation;
import net.smileycorp.cosmeticwood.common.WoodHandler;

public interface TileWood {
	
	default ResourceLocation getType() {
		return WoodHandler.getDefault();
	}
	
	default String getTypeString() {
		ResourceLocation type = getType();
		return (WoodHandler.isValidType(type) ? type : WoodHandler.getDefault()).toString();
	}
	
	void setType(ResourceLocation type);
	
	String getRegistryName();
	
}
