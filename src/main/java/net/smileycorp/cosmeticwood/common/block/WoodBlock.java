package net.smileycorp.cosmeticwood.common.block;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.smileycorp.atlas.api.block.PropertyOpenString;
import net.smileycorp.cosmeticwood.common.WoodHandler;

public interface WoodBlock {
	
	PropertyOpenString VARIANT = new PropertyOpenString("type", WoodHandler::contains);
	
	boolean isWood();
	
	ResourceLocation getDefaultType();
	
	TileEntity createNewTileEntity();
	
	String[] getModIds();
	
	ResourceLocation getType(IBlockAccess world, BlockPos pos);
	
}
