package net.smileycorp.cosmeticwood.plugins.vanilla;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.smileycorp.cosmeticwood.common.CWPlugin;
import net.smileycorp.cosmeticwood.plugins.vanilla.block.*;

@CWPlugin(modid = "minecraft")
public class PluginVanilla {
	
	public static Block WORKBENCH = new BlockCWWorkbench();
	public static Block BOOKSHELF = new BlockCWBookshelf();
	public static Block PRESSURE_PLATE = new BlockCWPressurePlate();
	public static Block BUTTON = new BlockCWButton();
	public static Block CHEST = new BlockCWChest(BlockChest.Type.BASIC);
	public static Block TRAPPED_CHEST = new BlockCWChest(BlockChest.Type.TRAP);
	
}
