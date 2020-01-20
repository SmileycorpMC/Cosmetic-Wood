package net.smileycorp.cosmeticwood.common.block;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.smileycorp.cosmeticwood.common.ModDefinitions;

public class CWBlocks {
	
	public static Block WORKBENCH = new BlockCWWorkbench();
	
	public static Block[] blocks = {WORKBENCH};
	
	public static void registerContent() {
		for (Block block:blocks) {
			ForgeRegistries.BLOCKS.register(block);
			ItemBlock item = new ItemBlock(block);
			item.setRegistryName(block.getRegistryName());
			ForgeRegistries.ITEMS.register(item);
		}
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void registerModels(ModelRegistryEvent event) {
		for (Block block : blocks) {
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(ModDefinitions.getResource(block.getRegistryName().getResourcePath()), "normal"));
		}
	}
	
}
