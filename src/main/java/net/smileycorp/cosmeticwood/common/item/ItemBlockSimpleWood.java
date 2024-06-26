package net.smileycorp.cosmeticwood.common.item;

import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.smileycorp.atlas.api.util.TextUtils;
import net.smileycorp.cosmeticwood.common.Constants;
import net.smileycorp.cosmeticwood.common.WoodHandler;

import javax.annotation.Nullable;
import java.util.List;

public class ItemBlockSimpleWood extends ItemBlock implements WoodItem {

	public ItemBlockSimpleWood(Block block) {
		super(block);
		setRegistryName(block.getRegistryName());
		hasSubtypes = true;
	}
	
	@Override
	public boolean isInCreativeTab(CreativeTabs tab) {
		return super.isInCreativeTab(tab) || isSubtypeTab(tab);
	}
	
	@Override
	public String getCreatorModId(ItemStack itemStack) {
		return Constants.MODID;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn){
    	NBTTagCompound nbt = stack.getTagCompound();
    	if (nbt != null && nbt.hasKey("type")) {
			String type = WoodHandler.fixData(nbt.getString("type")).getResourcePath();
			tooltip.add(TextUtils.toProperCase(type));
    	}
		else tooltip.add(TextUtils.toProperCase(WoodHandler.getDefault().getResourcePath()));
    }

}
