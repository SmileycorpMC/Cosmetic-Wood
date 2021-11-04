package net.smileycorp.cosmeticwood.plugin.tcon.block;

import javax.annotation.Nullable;

import net.minecraft.block.BlockHopper;
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
import net.smileycorp.cosmeticwood.common.block.IWoodBlock;
import net.smileycorp.cosmeticwood.plugins.tcon.tileentity.TileCWTconWhopper;
import slimeknights.tconstruct.gadgets.block.BlockWoodenHopper;

import com.google.common.collect.ImmutableList;

public class BlockCWTconWhopper extends BlockWoodenHopper implements IWoodBlock {

	public BlockCWTconWhopper() {
		super();
		setRegistryName("tconstruct", "wooden_hopper");
		setUnlocalizedName("tconstruct.wooden_hopper");
	}
	
	@Override
	public BlockStateContainer createBlockState() {
		return new ExtendedBlockState(this, new IProperty[]{BlockHopper.FACING}, new IUnlistedProperty[]{VARIANT});
	}
	
	@Override
	public ImmutableList<IBlockState> getBlockStates() {
		return createBlockState().getValidStates();
	}
	
	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
	    TileEntity te = world.getTileEntity(pos);
	    if(te != null && te instanceof TileCWTconWhopper) {
	    	return ((IExtendedBlockState)state).withProperty(VARIANT,((TileCWTconWhopper) te).getTypeString());
	    }
	    return super.getExtendedState(state, world, pos);
	 }
	
	@Override
	public boolean hasTileEntity(IBlockState state) {
        return true;
    }
	
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileCWTconWhopper();
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
		super.onBlockPlacedBy(world, pos, state, placer, stack);
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
    
    @Override
	public String getItemVariant() {
		return "normal";
	}

	@Override
	public Class getTile() {
		return TileCWTconWhopper.class;
	}

}
