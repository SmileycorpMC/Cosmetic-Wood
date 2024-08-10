package net.smileycorp.cosmeticwood.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.InventoryEnderChest;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.world.ChunkWatchEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.smileycorp.cosmeticwood.common.data.WoodHandler;
import net.smileycorp.cosmeticwood.common.data.WoodTypeStorage;
import net.smileycorp.cosmeticwood.common.network.PacketHandler;
import net.smileycorp.cosmeticwood.common.network.SyncWoodTypesMessage;
import net.smileycorp.cosmeticwood.common.registry.ContentRegistry;

@EventBusSubscriber(modid = Constants.MODID)
public class CommonProxy {

	public void preInit(FMLPreInitializationEvent event) {
		PacketHandler.initPackets();
		ConfigHandler.syncConfig(event);
		ContentRegistry.preInit(event.getAsmData());
	}

	public void init(FMLInitializationEvent event) {
		ContentRegistry.init();
	}

	public void postInit(FMLPostInitializationEvent event) {
		WoodHandler.getInstance().buildProperties();
		ContentRegistry.replaceRecipes();
	}
	
	@SubscribeEvent
	public void startTrackingChunk(ChunkWatchEvent.Watch event) {
		Chunk chunk = event.getChunkInstance();
		if (chunk == null) return;
		if (!chunk.hasCapability(WoodTypeStorage.CAPABILITY, null)) return;
		PacketHandler.CHANNEL.sendTo(new SyncWoodTypesMessage(chunk), event.getPlayer());
	}

	//fix data from old versions
	@SubscribeEvent
	public void playerJoin(PlayerLoggedInEvent event) {
		EntityPlayer player = event.player;
		World world = player.world;
		if (player == null || world == null) return;
		if (world.isRemote) return;
		InventoryPlayer inventory = player.inventory;
		if (inventory != null) {
			for (ItemStack stack : inventory.mainInventory) WoodHandler.getInstance().fixData(stack);
			for (ItemStack stack : inventory.offHandInventory) WoodHandler.getInstance().fixData(stack);
		}
		InventoryEnderChest inv = player.getInventoryEnderChest();
		if (inv != null) for (int i = 0; i < inv.getSizeInventory(); i++) WoodHandler.getInstance().fixData(inv.getStackInSlot(i));
	}
	
	@SubscribeEvent
	public void attachCapabilities(AttachCapabilitiesEvent<Chunk> event) {
		event.addCapability(Constants.loc("wood_types"), new WoodTypeStorage.Provider());
	}

}
