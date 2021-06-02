package net.smileycorp.cosmeticwood.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;

public class DummyWoodBlock<T extends Block, IWoodBlock> extends Block {

	private final Class<T> clazz;
	private final String modid;
	
	public DummyWoodBlock(Class<T> clazz, String modid) {
		super(Material.WOOD);
		this.clazz=clazz;
		this.modid=modid;
	}

	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this);
	}
	
	public T getInstance() throws Exception {
		return clazz.newInstance();
	}

	public String getModid() {
		return modid;
	}

}
