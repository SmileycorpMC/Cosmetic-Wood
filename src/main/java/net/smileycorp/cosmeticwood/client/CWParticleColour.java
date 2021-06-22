package net.smileycorp.cosmeticwood.client;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.smileycorp.cosmeticwood.common.WoodHandler;
import net.smileycorp.cosmeticwood.common.tileentity.ITileCW;

@SideOnly(Side.CLIENT)
public class CWParticleColour implements IBlockColor {

	@Override
	public int colorMultiplier(IBlockState state, IBlockAccess world, BlockPos pos, int tintIndex) {
		ResourceLocation variant = WoodHandler.getDefault();
		TileEntity te = world.getTileEntity(pos);
		if(te instanceof ITileCW) {
			variant = ((ITileCW) te).getType();
		}
		return WoodHandler.getColour(variant).getRGB();
	}

}
