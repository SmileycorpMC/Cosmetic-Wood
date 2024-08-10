package net.smileycorp.cosmeticwood.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.smileycorp.cosmeticwood.client.ClientProxy;
import net.smileycorp.cosmeticwood.common.data.WoodTypeStorage;

public class SyncWoodTypesMessage implements IMessage {
	
	private int x, z;
	private NBTTagCompound nbt;

	public SyncWoodTypesMessage() {}

	public SyncWoodTypesMessage(Chunk chunk) {
		this.x = chunk.x;
		this.z = chunk.z;
		nbt = chunk.getCapability(WoodTypeStorage.CAPABILITY, null).save();
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		x = buf.readInt();
		z = buf.readInt();
		nbt = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeByte(x);
		buf.writeByte(z);
		ByteBufUtils.writeTag(buf, nbt);
	}
	
	public IMessage process(MessageContext ctx) {
		if (ctx.side == Side.CLIENT) {
			Minecraft mc = Minecraft.getMinecraft();
			mc.addScheduledTask(() -> ClientProxy.syncChunk(x, z, nbt));
		}
		return null;
	}
	
}
