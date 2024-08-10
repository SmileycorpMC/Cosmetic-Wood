package net.smileycorp.cosmeticwood.common.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.smileycorp.cosmeticwood.common.Constants;

public class PacketHandler {

	public static SimpleNetworkWrapper CHANNEL;

	public static void initPackets() {
		CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel(Constants.MODID);
		CHANNEL.registerMessage(SyncWoodTypesMessage::process, SyncWoodTypesMessage.class, 1, Side.CLIENT);
	}
	
}
