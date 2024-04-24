package net.smileycorp.cosmeticwood.plugins.vanilla.block;

import net.minecraft.block.BlockChest;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.smileycorp.cosmeticwood.common.block.WoodBlock;
import net.smileycorp.cosmeticwood.common.tile.TileSimpleWood;
import net.smileycorp.cosmeticwood.plugins.vanilla.client.TESRCWChest;
import net.smileycorp.cosmeticwood.plugins.vanilla.tileentity.TileCWChest;

import javax.annotation.Nullable;

public class BlockCWChest extends BlockChest implements WoodBlock {

	private final Type type;

	public BlockCWChest(Type type) {
		super(type);
		setHardness(2.5F);
		setSoundType(SoundType.WOOD);
		setRegistryName("minecraft", type == Type.TRAP ? "trapped_chest" : "chest");
		setUnlocalizedName(type == Type.TRAP ? "chestTrap" : "chest");
		this.type = type;
	}

	@Override
	public BlockStateContainer createBlockState() {
		return new ExtendedBlockState(this, new IProperty[]{FACING}, new IUnlistedProperty[]{VARIANT});
	}

	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
		TileEntity te = world.getTileEntity(pos);
		if(te != null && te instanceof TileSimpleWood) return ((IExtendedBlockState)state).withProperty(VARIANT,((TileSimpleWood) te).getTypeString());
		return super.getExtendedState(state, world, pos);
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileCWChest(type);
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
		super.getDrops(drops, world, pos, state, fortune);
	}

	@Override
	public ItemStack getSilkTouchDrop(IBlockState state) {
		return WoodBlock.super.getSilkTouchDrop((IExtendedBlockState)state);
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		super.onBlockPlacedBy(world, pos, state, placer, stack);
		WoodBlock.super.onBlockPlacedBy(world, pos, state, placer, stack);
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		if (canConnect(source, pos, pos.north())) return NORTH_CHEST_AABB;
		if (canConnect(source, pos, pos.south())) return SOUTH_CHEST_AABB;
		if (canConnect(source, pos, pos.east())) return EAST_CHEST_AABB;
		if (canConnect(source, pos, pos.west())) return WEST_CHEST_AABB;
		return NOT_CONNECTED_AABB;
	}
	
	public boolean canPlaceBlockAt(World world, BlockPos pos) {
		return true;
	}
	
	@Override
	public ILockableContainer getContainer(World world, BlockPos pos, boolean allowBlocking) {
		TileEntity tileentity = world.getTileEntity(pos);
		if (!(tileentity instanceof TileCWChest)) return null;
		ILockableContainer te = (ILockableContainer) tileentity;
		if (!allowBlocking && this.isBlocked(world, pos)) return null;
		for (EnumFacing facing : EnumFacing.Plane.HORIZONTAL) {
			BlockPos blockpos = pos.offset(facing);
			if (!canConnect(world, pos, pos.offset(facing))) continue;
			if (!allowBlocking && isBlocked(world, blockpos)) return null;
			TileCWChest other = (TileCWChest) world.getTileEntity(pos.offset(facing));
			te = (facing == EnumFacing.EAST || facing == EnumFacing.SOUTH) ?
					new InventoryLargeChest("container.chestDouble", other, te) :
					new InventoryLargeChest("container.chestDouble", te, other);
		}
		return te;
	}
	
	private boolean canConnect(IBlockAccess source, BlockPos pos, BlockPos other) {
		if (source.getBlockState(other).getBlock() != this) return false;
		if (!(source.getTileEntity(pos) instanceof TileCWChest && source.getTileEntity(other) instanceof TileCWChest)) return false;
		if (((TileCWChest)source.getTileEntity(pos)).getType() == null) return false;
		return ((TileCWChest)source.getTileEntity(pos)).getType().equals(((TileCWChest)source.getTileEntity(other)).getType());
	}

	@Override
	public Class getTile() {
		return TileCWChest.class;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void initClient() {
		ClientRegistry.bindTileEntitySpecialRenderer(TileCWChest.class, new TESRCWChest());
	}

	//From BlockFlowerPot, should delay until drops are spawned, before block is broken
	@Override
	public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
		if (willHarvest) return true;
		return super.removedByPlayer(state, world, pos, player, willHarvest);
	}

	@Override
	public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack tool) {
		super.harvestBlock(world, player, pos, state, te, tool);
		world.setBlockToAir(pos);
	}
	
	@Override
	public ItemBlockChestCW getItem() {
		return new ItemBlockChestCW(this);
	}

}
