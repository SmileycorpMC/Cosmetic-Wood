package net.smileycorp.cosmeticwood.common;

import net.minecraft.block.Block;
import net.smileycorp.cosmeticwood.common.block.BlockCWBookshelf;
import net.smileycorp.cosmeticwood.common.block.BlockCWButton;
import net.smileycorp.cosmeticwood.common.block.BlockCWPressurePlate;
import net.smileycorp.cosmeticwood.common.block.BlockCWWorkbench;
import net.smileycorp.cosmeticwood.common.block.DummyWoodBlock;

public class CWContent {
	
	//Minecraft
	public static Block WORKBENCH = new DummyWoodBlock(BlockCWWorkbench.class, "minecraft");
	public static Block BOOKSHELF = new DummyWoodBlock(BlockCWBookshelf.class, "minecraft");
	public static Block PRESSURE_PLATE = new DummyWoodBlock(BlockCWPressurePlate.class, "minecraft");
	public static Block BUTTON = new DummyWoodBlock(BlockCWButton.class, "minecraft");
	
	//Rustic
	//public static Block RUSTIC_CHAIR = new DummyWoodBlock(BlockCWRusticChair.class, "rustic");
	
	//Tinkers Construct
	//public static Block TCON_WHOPPER = new DummyWoodBlock(BlockCWTconWhopper.class, "tconstruct");
}
