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
import net.smileycorp.cosmeticwood.common.tile.ITileCW;
import net.smileycorp.cosmeticwood.plugins.vanilla.block.BlockCWChest;

import javax.annotation.Nullable;

public class TileCWChest extends TileEntityChest implements ITileCW {

private ResourceLocation type = new ResourceLocation("oak");

	public TileCWChest() {}
	
	public TileCWChest(BlockChest.Type type) {
		super(type);
	}
	
	@Override
	public ResourceLocation getType() {
		return type.getResourcePath().isEmpty() ? WoodHandler.getDefault() : type;
	}
	
	@Override
	public void setType(ResourceLocation type) {
		this.type=type;
		this.markDirty();
	}
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return false;
    }
	
	@Override
	@Nullable
    protected TileEntityChest getAdjacentChest(EnumFacing side) {
        BlockPos blockpos = this.pos.offset(side);

        if (this.isChestAt(blockpos))
        {
            TileEntity tileentity = this.world.getTileEntity(blockpos);

            if (tileentity instanceof TileCWChest)
            {
            	TileCWChest tileentitychest = (TileCWChest)tileentity;
                tileentitychest.setNeighbor(this, side.getOpposite());
                return tileentitychest;
            }
        }

        return null;
    }
	
	private boolean isChestAt(BlockPos posIn) {
        if (this.world == null) {
            return false;
        } else {
        	IBlockState state = this.world.getBlockState(posIn);
            Block block = state.getBlock();
            if (block instanceof BlockCWChest && ((BlockChest)block).chestType == this.getChestType())
            	return ((ITileCW) this.world.getTileEntity(posIn)).getType().equals(this.getType());
            else return false;
        }
    }
	
	@SuppressWarnings("incomplete-switch")
    private void setNeighbor(TileEntityChest chestTe, EnumFacing side)
    {
        if (chestTe.isInvalid())
        {
            this.adjacentChestChecked = false;
        }
        else if (this.adjacentChestChecked)
        {
            switch (side)
            {
                case NORTH:

                    if (this.adjacentChestZNeg != chestTe)
                    {
                        this.adjacentChestChecked = false;
                    }

                    break;
                case SOUTH:

                    if (this.adjacentChestZPos != chestTe)
                    {
                        this.adjacentChestChecked = false;
                    }

                    break;
                case EAST:

                    if (this.adjacentChestXPos != chestTe)
                    {
                        this.adjacentChestChecked = false;
                    }

                    break;
                case WEST:

                    if (this.adjacentChestXNeg != chestTe)
                    {
                        this.adjacentChestChecked = false;
                    }
            }
        }
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
		return "CWChest";
	}
	
	

}
