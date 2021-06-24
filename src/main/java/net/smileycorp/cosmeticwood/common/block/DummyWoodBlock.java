package net.smileycorp.cosmeticwood.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class DummyWoodBlock<T extends Block, IWoodBlock> extends Block {

	private final Class<T> clazz;
	private final String modid;
	
	public DummyWoodBlock(Class<T> clazz) {
		this(clazz, "minecraft");
	}
	
	public DummyWoodBlock(Class<T> clazz, String modid) {
		super(Material.WOOD);
		this.clazz=clazz;
		this.modid=modid;
	}
	
	public T getInstance() throws Exception {
		return clazz.newInstance();
	}

	public String getModid() {
		return modid;
	}

}
