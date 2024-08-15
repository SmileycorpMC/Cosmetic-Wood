package net.smileycorp.cosmeticwood.common.registry.block;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.smileycorp.atlas.api.block.PropertyOpenString;
import net.smileycorp.cosmeticwood.common.WoodObject;
import net.smileycorp.cosmeticwood.common.data.WoodHandler;

public interface WoodBlock extends WoodObject {
	
	PropertyOpenString VARIANT = new PropertyOpenString("type", WoodHandler.getInstance()::contains);
	
	ResourceLocation getType(IBlockAccess world, BlockPos pos);
	
}
