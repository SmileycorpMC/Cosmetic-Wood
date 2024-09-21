package net.smileycorp.cosmeticwood.mixin;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.smileycorp.cosmeticwood.api.registry.WoodObject;
import net.smileycorp.cosmeticwood.api.registry.item.WoodStack;
import net.smileycorp.cosmeticwood.common.data.WoodTypeStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemBlock.class)
public class MixinItemBlock {
    
    @Inject(at=@At("HEAD"), method = "setTileEntityNBT")
    private static void CW$setTileEntityNBT(World world, EntityPlayer player, BlockPos pos, ItemStack stack, CallbackInfoReturnable<Boolean> callback) {
        if (!((WoodObject)(Object)stack).isWood()) return;
        WoodTypeStorage.setWoodType(world, pos, ((WoodStack)(Object)stack).getType());
    }
    
}
