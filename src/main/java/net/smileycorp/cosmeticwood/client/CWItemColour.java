package net.smileycorp.cosmeticwood.client;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.smileycorp.cosmeticwood.common.WoodHandler;

@SideOnly(Side.CLIENT)
public class CWItemColour implements IItemColor {

	@Override
	public int colorMultiplier(ItemStack stack, int tintIndex) {
		if (tintIndex == 0) {
			ResourceLocation variant = WoodHandler.getDefault();
			NBTTagCompound nbt = stack.getTagCompound();
			if(nbt.hasKey("type")) {
				variant = WoodHandler.fixData(nbt.getString("type"));
			}
			return WoodHandler.getColour(variant).getRGB();
		}
		return 0xFFFFFF;
	}

}
