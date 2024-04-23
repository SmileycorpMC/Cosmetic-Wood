package net.smileycorp.cosmeticwood.plugins.vanilla.block;

import net.minecraft.block.BlockButtonWood;
import net.minecraft.block.SoundType;
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
import net.smileycorp.cosmeticwood.common.block.WoodBlock;
import net.smileycorp.cosmeticwood.common.tile.TileSimpleWood;

import javax.annotation.Nullable;


public class BlockCWButton extends BlockButtonWood implements WoodBlock {

	public BlockCWButton() {
		super();
		setHardness(0.5F);
		setSoundType(SoundType.WOOD);
		setRegistryName("minecraft", "wooden_button");
		setUnlocalizedName("button");
	}

	@Override
	public BlockStateContainer createBlockState() {
		return new ExtendedBlockState(this, new IProperty[]{POWERED, FACING}, new IUnlistedProperty[]{VARIANT});
	}

	@Override
	public String getItemVariant() {
		return "inventory";
	}

	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
		TileEntity te = world.getTileEntity(pos);
		if(te != null && te instanceof TileSimpleWood) {
			return ((IExtendedBlockState)state).withProperty(VARIANT,((TileSimpleWood) te).getTypeString());
		}
		return super.getExtendedState(state, world, pos);
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileSimpleWood();
	}

	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list) {
		WoodBlock.super.getSubBlocks(tab, list);
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return WoodBlock.super.getPickBlock(state, target, world, pos, player);
	}

	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		WoodBlock.super.getDrops(drops, world, pos, state, fortune);
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		WoodBlock.super.onBlockPlacedBy(world, pos, state, placer, stack);
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
