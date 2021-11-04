package net.smileycorp.cosmeticwood.common.block;

import java.util.Set;
import java.util.function.Predicate;

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
import net.smileycorp.cosmeticwood.common.tile.ITileCW;
import net.smileycorp.cosmeticwood.common.tile.TileSimpleWood;

import com.google.common.collect.ImmutableList;

public interface IWoodBlock {

	public static PropertyOpenString VARIANT = new PropertyOpenString("type", new Predicate<String>(){
		@Override
		public boolean test(String type) {
			return WoodHandler.contains(type);
		}
		
	});	
	
	public abstract ImmutableList<IBlockState> getBlockStates();
	
	public default String[] getModids() {
		return new String[]{};
	}
	
	public default void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list) {
		Set<ResourceLocation> types = WoodHandler.getTypes(getModids());
		for(ResourceLocation type : types) {
	    	ItemStack stack = new ItemStack((Block) this);
	    	NBTTagCompound nbt = new NBTTagCompound();
	        nbt.setString("type", type.toString());
	        stack.setTagCompound(nbt);
	        list.add(stack);
		}
	}
	
	public default ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		ItemStack stack = new ItemStack((Block) this);
		NBTTagCompound tag = new NBTTagCompound();
		if (world.getTileEntity(pos) instanceof ITileCW) {
			tag.setString("type", ((ITileCW)world.getTileEntity(pos)).getTypeString());
			stack.setTagCompound(tag);
		}
		return stack;
    }
	
	public default ItemStack getPickBlock(ItemStack stack, IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		NBTTagCompound tag = new NBTTagCompound();
		if (world.getTileEntity(pos) instanceof ITileCW) {
			tag.setString("type", ((ITileCW) world.getTileEntity(pos)).getTypeString());
			stack.setTagCompound(tag);
		}
		return stack;
    }
	
	public default void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		ItemStack stack = new ItemStack((Block) this);
		NBTTagCompound tag = new NBTTagCompound();
		if (world.getTileEntity(pos) instanceof ITileCW) {
			tag.setString("type", ((ITileCW)world.getTileEntity(pos)).getTypeString());
			stack.setTagCompound(tag);
		}	
		drops.add(stack);
    }
	
	public default ItemStack getSilkTouchDrop(IExtendedBlockState state) {
		ResourceLocation type = WoodHandler.fixData(state.getValue(VARIANT));
		ItemStack stack = new ItemStack((Block) this);
    	NBTTagCompound nbt = new NBTTagCompound();
    	if (type!=null) {
        	nbt.setString("type", type.toString());
    	} else {
    		nbt.setString("type", WoodHandler.getDefault().toString());
    	}
        stack.setTagCompound(nbt);
        return stack; 
	}
	
	public default void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		NBTTagCompound nbt = stack.getTagCompound();
		if (nbt!=null) {
			if (nbt.hasKey("type")) {
				String type = nbt.getString("type");
				if (world.getTileEntity(pos) instanceof ITileCW) {
					((ITileCW) world.getTileEntity(pos)).setType(WoodHandler.fixData(type));
				}	
			}
		}
	}

	public default String getItemVariant() {
		return "normal";
	}
	
	public default Item getItem() {
		return new ItemBlockCW((Block) this);
	}

	public default Class<? extends TileEntity> getTile() {
		return TileSimpleWood.class;
	}
	
	@SideOnly(Side.CLIENT)
	public default void initClient() {}

}
