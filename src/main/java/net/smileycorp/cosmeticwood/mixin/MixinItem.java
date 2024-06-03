package net.smileycorp.cosmeticwood.mixin;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.smileycorp.cosmeticwood.common.Constants;
import net.smileycorp.cosmeticwood.common.WoodHandler;
import net.smileycorp.cosmeticwood.common.item.ModifiableWoodItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class MixinItem implements ModifiableWoodItem {
    
    private boolean isWood;
    private ResourceLocation defaultType = WoodHandler.getDefault();
    private String[] modIds = new String[0];
    
    @Override
    public boolean isWoodItem() {
        return isWood;
    }
    
    @Override
    public ResourceLocation getDefaultType() {
        return defaultType;
    }
    
    @Override
    public ResourceLocation getType(ItemStack stack) {
        if (stack == null) return null;
        if (!stack.hasTagCompound()) return getDefaultType();
        if (!stack.getTagCompound().hasKey("type")) return getDefaultType();
        return new ResourceLocation(stack.getTagCompound().getString("type"));
    }
    
    @Override
    public String[] getModIds() {
        return modIds;
    }
    
    @Override
    public void setWoodItem() {
        isWood = true;
    }
    
    @Override
    public void setDefault(ResourceLocation loc) {
        defaultType = loc;
    }
    
    @Override
    public void setModIds(String... modids) {
        this.modIds = modids;
    }
    
    @Inject(method = "getCreatorModId", at = @At("HEAD"), remap = false, cancellable = true)
    public void getCreatorModId(ItemStack itemStack, CallbackInfoReturnable<String> callback) {
        if (isWoodItem()) callback.setReturnValue(Constants.MODID);
    }
    
}
