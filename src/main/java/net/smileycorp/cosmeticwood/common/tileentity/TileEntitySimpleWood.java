package net.smileycorp.cosmeticwood.common.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntitySimpleWood extends TileEntity {
	
private String type;
	
	public TileEntitySimpleWood() {}

	public String getType() {
		if (type==null) {
			return "Null";
		}
		return type;
	}
	
	public void setType(String type) {
		this.type=type;
		this.markDirty();
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
       super.readFromNBT(compound);
       this.type = compound.getString("type");
    }

	@Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        if (type == null) {
        	type = "null";
        }
        compound.setString("type", type);
        return compound;
    }
	
    @Override
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound tag = super.getUpdateTag();
		tag.setString("type", type);
		return tag;
	}
	
	@Override
	public void handleUpdateTag(NBTTagCompound compound) {
		super.handleUpdateTag(compound);
		type = compound.getString("type");
	}
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(pos, getBlockMetadata(), getUpdateTag());
	}
}
