package net.smileycorp.cosmeticwood.common.registry.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreIngredient;
import net.smileycorp.cosmeticwood.api.registry.item.WoodStack;
import net.smileycorp.cosmeticwood.common.data.WoodHandler;

import java.util.Iterator;

public class WoodOreIngredient extends OreIngredient {
    
    public WoodOreIngredient(String ore, WoodStack wood) {
        super(ore);
        if (!wood.isWood()) return;
        Iterator<ItemStack> iterator = ores.iterator();
        while (iterator.hasNext()) {
            ItemStack stack = iterator.next();
            ResourceLocation type = WoodHandler.getInstance().getRegistry(stack);
            if (type == null) {
                iterator.remove();
                continue;
            }
            if (type.equals(wood.getDefaultType())) continue;
            for (String modid : wood.getModIds()) if (type.getResourceDomain().equals(modid)) {
                iterator.remove();
                break;
            }
        }
    }
    
}
