package net.smileycorp.cosmeticwood.plugins.vanilla;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest.Type;
import net.smileycorp.cosmeticwood.common.CWPlugin;
import net.smileycorp.cosmeticwood.plugins.vanilla.block.BlockCWBookshelf;
import net.smileycorp.cosmeticwood.plugins.vanilla.block.BlockCWButton;
import net.smileycorp.cosmeticwood.plugins.vanilla.block.BlockCWChest;
import net.smileycorp.cosmeticwood.plugins.vanilla.block.BlockCWPressurePlate;
import net.smileycorp.cosmeticwood.plugins.vanilla.block.BlockCWWorkbench;

@CWPlugin(modid = "minecraft")
public class PluginVanilla {
	
	public static Block WORKBENCH = new BlockCWWorkbench();
	public static Block BOOKSHELF = new BlockCWBookshelf();
	public static Block PRESSURE_PLATE = new BlockCWPressurePlate();
	public static Block BUTTON = new BlockCWButton();
	public static Block CHEST = new BlockCWChest(Type.BASIC);
	
}
