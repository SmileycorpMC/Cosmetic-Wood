package net.smileycorp.cosmeticwood.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.smileycorp.atlas.api.block.PropertyOpenString;
import net.smileycorp.cosmeticwood.common.WoodHandler;
import net.smileycorp.cosmeticwood.common.tile.TileSimpleWood;
import net.smileycorp.cosmeticwood.common.tile.TileWood;

import java.util.Set;

public interface WoodBlock {

	PropertyOpenString VARIANT = new PropertyOpenString("type", WoodHandler::contains);

	default String[] getModids() {
		return new String[]{};
	}

	default void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list) {
		Set<ResourceLocation> types = WoodHandler.getTypes(getModids());
		for(ResourceLocation type : types) {
			ItemStack stack = new ItemStack((Block) this);
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setString("type", type.toString());
			stack.setTagCompound(nbt);
			list.add(stack);
		}
	}

	default ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		ItemStack stack = new ItemStack((Block) this);
		NBTTagCompound tag = new NBTTagCompound();
		if (world.getTileEntity(pos) instanceof TileWood) {
			tag.setString("type", ((TileWood)world.getTileEntity(pos)).getTypeString());
			stack.setTagCompound(tag);
		}
		return stack;
	}

	default void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		ItemStack stack = new ItemStack((Block) this);
		NBTTagCompound tag = new NBTTagCompound();
		if (world.getTileEntity(pos) instanceof TileWood) {
			tag.setString("type", ((TileWood)world.getTileEntity(pos)).getTypeString());
			stack.setTagCompound(tag);
		}
		drops.add(stack);
	}

	default ItemStack getSilkTouchDrop(IExtendedBlockState state) {
		ResourceLocation type = WoodHandler.fixData(state.getValue(VARIANT));
		ItemStack stack = new ItemStack((Block) this);
		NBTTagCompound nbt = new NBTTagCompound();
		if (type != null) nbt.setString("type", type.toString());
		else nbt.setString("type", WoodHandler.getDefault().toString());
		stack.setTagCompound(nbt);
		return stack;
	}

	 default void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		NBTTagCompound nbt = stack.getTagCompound();
		if (nbt!=null) {
			if (nbt.hasKey("type")) {
				String type = nbt.getString("type");
				if (world.getTileEntity(pos) instanceof TileWood) {
					((TileWood) world.getTileEntity(pos)).setType(WoodHandler.fixData(type));
				}
			}
		}
	}

	default String getItemVariant() {
		return "normal";
	}

	default Item getItem() {
		return new ItemBlockWood((Block) this);
	}

	default <T extends TileEntity & TileWood> Class<T> getTile() {
		return (Class<T>) TileSimpleWood.class;
	}

	@SideOnly(Side.CLIENT)
	default void initClient() {}
    
    default ResourceLocation getDefaultType() {
		return WoodHandler.getDefault();
	}
	
}
