package net.smileycorp.cosmeticwood.common.block;

import java.util.List;
import java.util.function.Predicate;

import net.minecraft.block.Block;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.smileycorp.atlas.api.block.PropertyOpenString;
import net.smileycorp.cosmeticwood.common.ModdedWoodHandler;
import net.smileycorp.cosmeticwood.common.WoodHandler;
import net.smileycorp.cosmeticwood.common.tileentity.TileEntitySimpleWood;

public interface IWoodBlock {

	public static PropertyOpenString VARIANT = new PropertyOpenString("type", new Predicate<String>(){
		@Override
		public boolean test(String type) {
			return WoodHandler.getTypes().contains(type);
		}
		
	});	
	
	public abstract BlockStateContainer createBlockState();
	
	public default void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list) {
		getSubBlocks(tab, list, null);
	}
	
	public default void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list, String modid) {
		List<String> types = modid == null ? ModdedWoodHandler.getTypes(modid) : WoodHandler.getTypes();
		for(String type : types) {
	    	ItemStack stack = new ItemStack((Block) this);
	    	NBTTagCompound nbt = new NBTTagCompound();
	        nbt.setString("type", type);
	        stack.setTagCompound(nbt);
	        list.add(stack);
		}
	}
	
	public default ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		ItemStack stack = new ItemStack((Block) this);
		NBTTagCompound tag = new NBTTagCompound();
		if (world.getTileEntity(pos) instanceof TileEntitySimpleWood) {
			tag.setString("type", ((TileEntitySimpleWood)world.getTileEntity(pos)).getType());
			stack.setTagCompound(tag);
		}
		return stack;
    }
	
	public default ItemStack getPickBlock(ItemStack stack, IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		NBTTagCompound tag = new NBTTagCompound();
		if (world.getTileEntity(pos) instanceof TileEntitySimpleWood) {
			tag.setString("type", ((TileEntitySimpleWood)world.getTileEntity(pos)).getType());
			stack.setTagCompound(tag);
		}
		return stack;
    }
	
	public default void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		ItemStack stack = new ItemStack((Block) this);
		NBTTagCompound tag = new NBTTagCompound();
		if (world.getTileEntity(pos) instanceof TileEntitySimpleWood) {
			tag.setString("type", ((TileEntitySimpleWood)world.getTileEntity(pos)).getType());
			stack.setTagCompound(tag);
		}	
		drops.add(stack);
    }
	
	public default ItemStack getSilkTouchDrop(IExtendedBlockState state) {
		String type = state.getValue(VARIANT);
		ItemStack stack = new ItemStack((Block) this);
    	NBTTagCompound nbt = new NBTTagCompound();
    	if (type!=null) {
        	nbt.setString("type", type);
    	} else {
    		nbt.setString("type", WoodHandler.getDefault());
    	}
        stack.setTagCompound(nbt);
        return stack; 
	}
	
	public default void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
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

	public default String getItemVariant() {
		return "normal";
	}

}
