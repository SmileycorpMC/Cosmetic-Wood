package net.smileycorp.cosmeticwood.common.block;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.smileycorp.atlas.api.util.TextUtils;

public class ItemBlockCW extends ItemBlock {

	public ItemBlockCW(Block block) {
		super(block);
		setRegistryName(block.getRegistryName());
		hasSubtypes = true;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn){
    	NBTTagCompound nbt = stack.getTagCompound();
    	if (nbt!=null) {
    		if (nbt.hasKey("type")) {
    			tooltip.add(TextUtils.toProperCase(nbt.getString("type")));
    		}
    	}
    }

}
