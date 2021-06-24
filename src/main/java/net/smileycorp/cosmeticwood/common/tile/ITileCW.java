package net.smileycorp.cosmeticwood.common.tile;

import net.minecraft.util.ResourceLocation;
import net.smileycorp.cosmeticwood.common.WoodHandler;

public interface ITileCW {
	
	public default ResourceLocation getType() {
		return WoodHandler.getDefault();
	}
	
	public default String getTypeString() {
		return getType().toString();
	}
	
	public abstract void setType(ResourceLocation type);
	
	public abstract String getRegistryName();
}
