package net.smileycorp.cosmeticwood.client;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.smileycorp.cosmeticwood.common.data.WoodHandler;
import net.smileycorp.cosmeticwood.common.data.WoodTypeStorage;

@SideOnly(Side.CLIENT)
public class CWBlockColour implements IBlockColor {

	@Override
	public int colorMultiplier(IBlockState state, IBlockAccess world, BlockPos pos, int tintIndex) {
		ResourceLocation variant = WoodHandler.getDefault();
		if (world != null) variant = WoodTypeStorage.getWoodType(world, pos);
		return WoodHandler.getInstance().getColour(variant).getRGB();
	}

}
