package net.smileycorp.cosmeticwood.common;

import net.minecraft.block.Block;
import net.smileycorp.cosmeticwood.common.block.BlockCWBookshelf;
import net.smileycorp.cosmeticwood.common.block.BlockCWButton;
import net.smileycorp.cosmeticwood.common.block.BlockCWPressurePlate;
import net.smileycorp.cosmeticwood.common.block.BlockCWWorkbench;

@CWPlugin(modid = "minecraft")
public class PluginVanilla {
	
	public static Block WORKBENCH = new BlockCWWorkbench();
	public static Block BOOKSHELF = new BlockCWBookshelf();
	public static Block PRESSURE_PLATE = new BlockCWPressurePlate();
	public static Block BUTTON = new BlockCWButton();
	
}
