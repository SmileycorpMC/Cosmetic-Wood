package net.smileycorp.cosmeticwood.integration.jei;

import mezz.jei.api.ISubtypeRegistry.ISubtypeInterpreter;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.smileycorp.cosmeticwood.common.WoodHandler;

public class CWItemInterpreter implements ISubtypeInterpreter {

	public static ISubtypeInterpreter INSTANCE =  new CWItemInterpreter();

	@Override
	public String apply(ItemStack stack) {
		NBTTagCompound tag = stack.getTagCompound();
		if (tag!=null) {
			if (tag.hasKey("type")) {
				ResourceLocation loc = WoodHandler.fixData(tag.getString("type"));
				return Loader.instance().getCustomModProperties(loc.getResourceDomain()).get("name");
			}
		}
		return stack.getItem().getRegistryName().getResourceDomain();
	}

}
