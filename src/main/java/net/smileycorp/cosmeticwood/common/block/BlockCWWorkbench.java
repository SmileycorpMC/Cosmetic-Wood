package net.smileycorp.cosmeticwood.common.block;

import net.minecraft.block.BlockWorkbench;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.smileycorp.cosmeticwood.common.EnumBuilder;

public class BlockCWWorkbench extends BlockWorkbench {
	
	public BlockCWWorkbench() {
		super();
		this.setDefaultState(this.getDefaultState().withProperty(EnumBuilder.PropertyWoodType, EnumBuilder.getDefault()));
	}
	
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		if (placer instanceof EntityPlayer) {
			ItemStack stack = placer.getHeldItem(hand);
	        NBTTagCompound tag = stack.getTagCompound();
			if (tag!=null) {
				String key = tag.getString("type");
				return	this.getDefaultState().withProperty(EnumBuilder.PropertyWoodType, EnumBuilder.getType(key));
			}
			return this.getDefaultState();
		}
		return this.getDefaultState();
    }
	
	@Override
	protected BlockStateContainer createBlockState() {
		 return new BlockStateContainer(this, new IProperty[]{EnumBuilder.PropertyWoodType});
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
        return 0;
    }
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(EnumBuilder.PropertyWoodType, EnumBuilder.getDefault());
    }
	
	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        ItemStack stack = new ItemStack(this);
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("type", ((EnumBuilder.WoodType)state.getProperties().get(EnumBuilder.PropertyWoodType)).name());
        stack.setTagCompound(nbt);
		return stack;
	}
	
	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list) {
		for(EnumBuilder.WoodType type : EnumBuilder.WoodType.values()) {
	    	ItemStack stack = new ItemStack(this);
	    	NBTTagCompound nbt = new NBTTagCompound();
	        nbt.setString("type", type.name());
	        stack.setTagCompound(nbt);
	        list.add(stack);
		}
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		NBTTagCompound tag = stack.getTagCompound();
		if (tag!=null) {
			String key = tag.getString("type");
			state = state.withProperty(EnumBuilder.PropertyWoodType, EnumBuilder.getType(key));
		}
	}

}
