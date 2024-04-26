package net.smileycorp.cosmeticwood.common.tile;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.smileycorp.cosmeticwood.common.WoodHandler;

public class TileSimpleWood extends TileEntity implements TileWood {
	
	private ResourceLocation type = new ResourceLocation("oak");
	
	public TileSimpleWood() {}
	
	@Override
	public ResourceLocation getType() {
		return type.getResourcePath().isEmpty() ? WoodHandler.getDefault() : type;
	}
	
	@Override
	public void setType(ResourceLocation type) {
		this.type = type;
		this.markDirty();
	}
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return false;
    }
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
       super.readFromNBT(compound);
       this.type = WoodHandler.fixData(compound.getString("type"));
    }

	@Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setString("type", type.toString());
        return compound;
    }
	
    @Override
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound tag = super.getUpdateTag();
		tag.setString("type", type.toString());
		return tag;
	}
	
	@Override
	public void handleUpdateTag(NBTTagCompound compound) {
		super.handleUpdateTag(compound);
		type = WoodHandler.fixData(compound.getString("type"));
	}
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(pos, getBlockMetadata(), getUpdateTag());
	}

	@Override
	public String getRegistryName() {
		return "SimpleWood";
	}
	
}
