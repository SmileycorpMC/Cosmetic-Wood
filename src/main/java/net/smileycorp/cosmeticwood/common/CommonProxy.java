package net.smileycorp.cosmeticwood.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.InventoryEnderChest;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

@EventBusSubscriber(modid = Constants.MODID)
public class CommonProxy {

	public void preInit(FMLPreInitializationEvent event) {
		ConfigHandler.syncConfig(event);
		ContentRegistry.preInit(event.getAsmData());
	}

	public void init(FMLInitializationEvent event) {}

	public void postInit(FMLPostInitializationEvent event) {
		WoodHandler.buildProperties();
		ContentRegistry.replaceRecipes();
	}

	//fix data from old versions
	@SubscribeEvent
	public void playerJoin(PlayerLoggedInEvent event) {
		EntityPlayer player = event.player;
		World world = player.world;
		if (player != null && world != null) {
			if (!world.isRemote) {
				InventoryPlayer inventory = player.inventory;
				if (inventory != null) {
					for (ItemStack stack : inventory.mainInventory) WoodHandler.fixData(stack);
					for (ItemStack stack : inventory.offHandInventory) WoodHandler.fixData(stack);
				}
			}
			InventoryEnderChest inv = player.getInventoryEnderChest();
			if (inv != null) for (int i = 0; i < inv.getSizeInventory(); i++) WoodHandler.fixData(inv.getStackInSlot(i));
		}
	}

}
