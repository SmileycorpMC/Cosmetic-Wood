package net.smileycorp.cosmeticwood.common.block;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.smileycorp.atlas.api.client.CustomStateMapper;
import net.smileycorp.cosmeticwood.common.ModDefinitions;


@EventBusSubscriber(modid=ModDefinitions.modid)
public class CWBlocks {
	//Minecraft
	public static Block WORKBENCH = new DummyWoodBlock(BlockCWWorkbench.class, "minecraft");
	public static Block BOOKSHELF = new DummyWoodBlock(BlockCWBookshelf.class, "minecraft");
	public static Block PRESSURE_PLATE = new DummyWoodBlock(BlockCWPressurePlate.class, "minecraft");
	public static Block BUTTON = new DummyWoodBlock(BlockCWButton.class, "minecraft");
	
	//Rustic
	//public static Block RUSTIC_CHAIR = new DummyWoodBlock(BlockCWRusticChair.class, "rustic");
	
	//Tinkers Construct
	//public static Block TCON_WHOPPER = new DummyWoodBlock(BlockCWTconWhopper.class, "tconstruct");
	
	public static List<Block> blocks = new ArrayList<Block>();
	
	public static void registerContent() {
		Field[] fields = CWBlocks.class.getFields();
		for (Field field : fields) {
			try {
				Object o = field.get(new Object());
				if (o!=null && o instanceof DummyWoodBlock) {
					if (Loader.isModLoaded(((DummyWoodBlock) o).getModid()));
					o = ((DummyWoodBlock) o).getInstance();
					field.set(null, o);
				}
				if (o!=null && o instanceof Block &!(o instanceof DummyWoodBlock)) {
					blocks.add((Block) o);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		for (Block block : blocks) {
			if (block instanceof IWoodBlock && Loader.isModLoaded(block.getRegistryName().getResourceDomain())) {
				ForgeRegistries.BLOCKS.register(block);
				ForgeRegistries.ITEMS.register(new ItemBlockCW(block));
			}
		}
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void registerModels(ModelRegistryEvent event) {
		for (Block block : blocks) {
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(ModDefinitions.getResource(block), ((IWoodBlock)block).getItemVariant()));
			ModelLoader.setCustomStateMapper(block, new CustomStateMapper(ModDefinitions.modid, block.getRegistryName().getResourcePath()));
		}
	}
	
}
