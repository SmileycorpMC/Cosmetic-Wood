package net.smileycorp.cosmeticwood.common.block;

import javax.annotation.Nullable;

import net.minecraft.block.BlockPressurePlate;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.smileycorp.cosmeticwood.common.tileentity.TileEntitySimpleWood;

import com.google.common.collect.ImmutableList;

public class BlockCWPressurePlate extends BlockPressurePlate implements IWoodBlock {

	public BlockCWPressurePlate() {
		super(Material.WOOD, Sensitivity.EVERYTHING);
		setHardness(0.5F);
		setSoundType(SoundType.WOOD);
		setRegistryName("minecraft", "wooden_pressure_plate");
		setUnlocalizedName("pressurePlateWood");
	}
	
	@Override
	public BlockStateContainer createBlockState() {
		return new ExtendedBlockState(this, new IProperty[]{POWERED}, new IUnlistedProperty[]{VARIANT});
	}
	
	@Override
	public ImmutableList<IBlockState> getBlockStates() {
		return createBlockState().getValidStates();
	}
	
	@Override
	public String getItemVariant() {
		return "powered=false";
	}
	
	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
	    TileEntity te = world.getTileEntity(pos);
	    if(te != null && te instanceof TileEntitySimpleWood) {
	    	return ((IExtendedBlockState)state).withProperty(VARIANT,((TileEntitySimpleWood) te).getType());
	    }
	    return super.getExtendedState(state, world, pos);
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
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list) {
		IWoodBlock.super.getSubBlocks(tab, list);
	}
	
	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return IWoodBlock.super.getPickBlock(state, target, world, pos, player);
    }
	
	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		IWoodBlock.super.getDrops(drops, world, pos, state, fortune);
    }
	
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		IWoodBlock.super.onBlockPlacedBy(world, pos, state, placer, stack);
	}
	
	//From BlockFlowerPot, should delay until drops are spawned, before block is broken
    @Override
	public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest)
    {
        if (willHarvest) return true; 
        return super.removedByPlayer(state, world, pos, player, willHarvest);
    }
    
    @Override
	public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack tool) {
        super.harvestBlock(world, player, pos, state, te, tool);
        world.setBlockToAir(pos);
    }
  
}
