package net.smileycorp.cosmeticwood.common.block;

import java.util.List;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.smileycorp.atlas.api.block.PropertyString;
import net.smileycorp.cosmeticwood.common.WoodHandler;
import net.smileycorp.cosmeticwood.common.tileentity.TileEntitySimpleWood;

public class BlockCW extends Block {

	private final Block block;
	public static PropertyString VARIANT = new PropertyString("type", new Predicate<String>(){
		@Override
		public boolean test(String type) {
			return WoodHandler.getTypes().contains(type);
		}
		
	});
	
	public BlockCW(Block block) {
		super(block.getMaterial(block.getDefaultState()));
		this.block = block;
		this.setCreativeTab(CreativeTabs.DECORATIONS);
		this.setSoundType(block.getSoundType());
	}
	
	@Override
	  protected BlockStateContainer createBlockState() {
	    return new ExtendedBlockState(this, new IProperty[0], new IUnlistedProperty[]{VARIANT});
	  }
	
	@Override
	public int getMetaFromState(IBlockState state) {
        return 0;
    }
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState();
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }
	
	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list) {
		for(String type : WoodHandler.getTypes()) {
	    	ItemStack stack = new ItemStack(this);
	    	NBTTagCompound nbt = new NBTTagCompound();
	        nbt.setString("type", type);
	        stack.setTagCompound(nbt);
	        list.add(stack);
		}
	}
	
	@Deprecated
    public float getBlockHardness(IBlockState state, World world, BlockPos pos) {
        return block.getBlockHardness(state, world, pos);
    }
	
	@Override
	public boolean hasTileEntity(IBlockState state) {
        return true;
    }
	
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileEntitySimpleWood();
	}
	
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
        super.breakBlock(world, pos, state);
        world.removeTileEntity(pos);
    }
	
	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		ItemStack stack = new ItemStack(this);
		NBTTagCompound tag = new NBTTagCompound();
		if (world.getTileEntity(pos) instanceof TileEntitySimpleWood) {
			tag.setString("type", ((TileEntitySimpleWood)world.getTileEntity(pos)).getType());
			stack.setTagCompound(tag);
		}
		return stack;
    }
	
	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		ItemStack stack = new ItemStack(this);
		NBTTagCompound tag = new NBTTagCompound();
		if (world.getTileEntity(pos) instanceof TileEntitySimpleWood) {
			tag.setString("type", ((TileEntitySimpleWood)world.getTileEntity(pos)).getType());
			stack.setTagCompound(tag);
		}	
		drops.add(stack);
    }
	
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		NBTTagCompound nbt = stack.getTagCompound();
		if (nbt!=null) {
			if (nbt.hasKey("type")) {
				String type = nbt.getString("type");
				if (world.getTileEntity(pos) instanceof TileEntitySimpleWood) {
					((TileEntitySimpleWood) world.getTileEntity(pos)).setType(type);
				}	
			}
		}
	}
	
	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
	    TileEntity te = world.getTileEntity(pos);
	    if(te != null && te instanceof TileEntitySimpleWood) {
	    	return ((IExtendedBlockState)state).withProperty(VARIANT,((TileEntitySimpleWood) te).getType());
	    }
	    return super.getExtendedState(state, world, pos);
	 }
	
	//From BlockFlowerPot, should delay until drops are spawned, before block is broken
	@Override
    public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest)
    {
        if (willHarvest) return true; 
        return super.removedByPlayer(state, world, pos, player, willHarvest);
    }
    @Override
    public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack tool)
    {
        super.harvestBlock(world, player, pos, state, te, tool);
        world.setBlockToAir(pos);
    }

}
