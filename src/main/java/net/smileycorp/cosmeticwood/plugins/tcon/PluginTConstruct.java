package net.smileycorp.cosmeticwood.plugins.tcon;

import net.minecraft.block.Block;
import net.smileycorp.cosmeticwood.common.CWPlugin;
import net.smileycorp.cosmeticwood.plugins.tcon.block.BlockCWTconWhopper;

@CWPlugin(modid="tconstruct")
public class PluginTConstruct {
	public static Block TCON_WHOPPER = new BlockCWTconWhopper();
}
