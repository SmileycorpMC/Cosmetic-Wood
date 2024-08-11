package net.smileycorp.cosmeticwood.mixin;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.smileycorp.cosmeticwood.common.Constants;
import net.smileycorp.cosmeticwood.common.registry.item.WoodItem;
import net.smileycorp.cosmeticwood.common.registry.item.WoodStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class MixinItemStack implements WoodStack {
    
    @Shadow @Final private Item item;
    
    @Shadow public abstract boolean hasDisplayName();
    
    @Shadow public abstract int getMetadata();
    
    @Override
    public boolean isWoodItem() {
        if (item == null || getMetadata() > 0) return false;
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
    
    @Inject(at = @At("TAIL"), method = "getDisplayName", cancellable = true)
    public void CW$getDisplayName(CallbackInfoReturnable<String> callback) {
        if (!isWoodItem() || hasDisplayName()) return;
        callback.setReturnValue(callback.getReturnValue().replace(Constants.toProperCase(getDefaultType().getResourcePath()), "").trim());
    }
    
}
