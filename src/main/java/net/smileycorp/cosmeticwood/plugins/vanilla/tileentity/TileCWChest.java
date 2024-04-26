package net.smileycorp.cosmeticwood.plugins.vanilla.tileentity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.smileycorp.cosmeticwood.common.WoodHandler;
import net.smileycorp.cosmeticwood.common.tile.TileWood;
import net.smileycorp.cosmeticwood.plugins.vanilla.block.BlockCWChest;

import javax.annotation.Nullable;

public class TileCWChest extends TileEntityChest implements TileWood {

    private ResourceLocation type = null;

	public TileCWChest() {}
	
	public TileCWChest(BlockChest.Type type) {
		super(type);
	}
	
	@Override
	public ResourceLocation getType() {
        if (type == null) return null;
		return type.getResourcePath().isEmpty() ? WoodHandler.getDefault() : type;
	}
	
	@Override
	public void setType(ResourceLocation type) {
		this.type = type;
        adjacentChestChecked = false;
        if (adjacentChestZNeg != null) adjacentChestZNeg.adjacentChestChecked = false;
        if (adjacentChestZPos != null) adjacentChestZPos.adjacentChestChecked = false;
        if (adjacentChestXNeg != null) adjacentChestXNeg.adjacentChestChecked = false;
        if (adjacentChestXPos != null) adjacentChestXPos.adjacentChestChecked = false;
        adjacentChestZNeg = null;
        adjacentChestXPos = null;
        adjacentChestXNeg = null;
        adjacentChestZPos = null;
		markDirty();
        checkForAdjacentChests();
	}
    
    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return false;
    }
    
    @Override
    public void checkForAdjacentChests() {
        if (type != null) super.checkForAdjacentChests();
    }
	
	@Override
	@Nullable
    protected TileEntityChest getAdjacentChest(EnumFacing side) {
        if (type == null) return null;
        BlockPos blockpos = pos.offset(side);
        if (isChestAt(blockpos)) {
            TileEntity tileentity = world.getTileEntity(blockpos);
            if (tileentity instanceof TileCWChest) {
            	TileCWChest tileentitychest = (TileCWChest)tileentity;
                tileentitychest.setNeighbor(this, side.getOpposite());
                return tileentitychest;
            }
        }
        return null;
    }
	
	private boolean isChestAt(BlockPos pos) {
        if (this.world == null) return false;
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        if (!(block instanceof BlockCWChest && ((BlockChest) block).chestType == getChestType())) return false;
        return type.equals(((TileCWChest) world.getTileEntity(pos)).getType());
    }
    
	@SuppressWarnings("incomplete-switch")
    private void setNeighbor(TileEntityChest te, EnumFacing side) {
        if (!(te instanceof TileCWChest)) {
            adjacentChestChecked = false;
            return;
        }
        if (te.isInvalid() || ((TileCWChest) te).getType() != type) {
            adjacentChestChecked = false;
            return;
        }
        if (adjacentChestChecked) {
            switch (side) {
                case NORTH:
                    if (adjacentChestZNeg != te) adjacentChestChecked = false;
                    break;
                case SOUTH:
                    if (adjacentChestZPos != te) adjacentChestChecked = false;
                    break;
                case EAST:
                    if (adjacentChestXPos != te) adjacentChestChecked = false;
                    break;
                case WEST:
                    if (adjacentChestXNeg != te) adjacentChestChecked = false;
            }
        }
    }
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
       super.readFromNBT(compound);
       if (compound.hasKey("type")) type = WoodHandler.fixData(compound.getString("type"));
    }

	@Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        if (type != null) compound.setString("type", type.toString());
        return compound;
    }
	
    @Override
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound tag = super.getUpdateTag();
        if (type != null) tag.setString("type", type.toString());
		return tag;
	}
	
	@Override
	public void handleUpdateTag(NBTTagCompound compound) {
		super.handleUpdateTag(compound);
		if (compound.hasKey("type")) type = WoodHandler.fixData(compound.getString("type"));
	}
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return world == null ? null : new SPacketUpdateTileEntity(pos, getBlockMetadata(), getUpdateTag());
	}

	@Override
	public String getRegistryName() {
		return "CWChest";
	}

}
