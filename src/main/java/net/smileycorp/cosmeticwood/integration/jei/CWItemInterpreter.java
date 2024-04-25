package net.smileycorp.cosmeticwood.integration.jei;

import mezz.jei.api.ISubtypeRegistry.ISubtypeInterpreter;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.smileycorp.cosmeticwood.common.ConfigHandler;
import net.smileycorp.cosmeticwood.common.WoodHandler;

public class CWItemInterpreter implements ISubtypeInterpreter {

	@Override
	public String apply(ItemStack stack) {
		if (!ConfigHandler.displayVariantsInJei) return "";
		ResourceLocation type = WoodHandler.getRegistry(stack);
		return stack == null ? "" : type.toString();
	}

}
