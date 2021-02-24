package net.smileycorp.cosmeticwood.common.block;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.smileycorp.atlas.api.client.CustomStateMapper;
import net.smileycorp.cosmeticwood.common.ModDefinitions;


@EventBusSubscriber(modid=ModDefinitions.modid)
public class CWBlocks {
	public static BlockCW WORKBENCH = new BlockCWWorkbench();
	public static BlockCW PRESSURE_PLATE = new BlockCWPressurePlate();
	public static BlockCW BUTTON;
	public static Block[] blocks = {WORKBENCH, PRESSURE_PLATE};
	
	public static void registerContent() {
		for (Block block : blocks) {
			if (block instanceof BlockCW)
				ForgeRegistries.BLOCKS.register(block);
				ForgeRegistries.ITEMS.register(new ItemBlockCW(block));
		}
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void registerModels(ModelRegistryEvent event) {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(WORKBENCH), 0, new ModelResourceLocation(ModDefinitions.getResource("crafting_table"), "normal"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(PRESSURE_PLATE), 0, new ModelResourceLocation(ModDefinitions.getResource("wooden_pressure_plate"), "powered=false"));
		
		ModelLoader.setCustomStateMapper(WORKBENCH, new CustomStateMapper(ModDefinitions.modid, "crafting_table"));
		ModelLoader.setCustomStateMapper(PRESSURE_PLATE, new CustomStateMapper(ModDefinitions.modid, "wooden_pressure_plate"));
	}
	
}
