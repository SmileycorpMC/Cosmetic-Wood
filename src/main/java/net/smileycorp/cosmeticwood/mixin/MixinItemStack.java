package net.smileycorp.cosmeticwood.mixin;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.smileycorp.cosmeticwood.common.item.WoodStack;
import net.smileycorp.cosmeticwood.common.item.WoodItem;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ItemStack.class)
public class MixinItemStack implements WoodStack {
    
    @Shadow @Final private Item item;
    
    @Override
    public boolean isWoodItem() {
        return ((WoodItem)item).isWoodItem();
    }
    
    @Override
    public ResourceLocation getDefaultType() {
        return ((WoodItem)item).getDefaultType();
    }
    
    @Override
    public ResourceLocation getType(ItemStack stack) {
        return ((WoodItem)item).getType(stack);
    }
    
    @Override
    public String[] getModIds() {
        return ((WoodItem)item).getModIds();
    }
    
    @Override
    public ResourceLocation getType() {
        return getType((ItemStack)(Object)this);
    }
    
}
