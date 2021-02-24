package net.smileycorp.cosmeticwood.common.tileentity;

public interface ITileCW {
	
	public default String getType() {
		return "oak";
	}
	
	public abstract void setType(String type);
}
