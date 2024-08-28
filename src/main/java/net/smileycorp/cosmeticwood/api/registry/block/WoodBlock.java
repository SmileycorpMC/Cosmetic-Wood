package net.smileycorp.cosmeticwood.api.registry.block;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.smileycorp.cosmeticwood.api.registry.WoodObject;

public interface WoodBlock extends WoodObject {
	
	ResourceLocation getType(IBlockAccess world, BlockPos pos);
	
}
