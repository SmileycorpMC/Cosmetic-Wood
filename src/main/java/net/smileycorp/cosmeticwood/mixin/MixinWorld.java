package net.smileycorp.cosmeticwood.mixin;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.smileycorp.cosmeticwood.api.registry.WoodObject;
import net.smileycorp.cosmeticwood.common.data.WoodHandler;
import net.smileycorp.cosmeticwood.common.data.WoodTypeStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(World.class)
public abstract class MixinWorld implements IBlockAccess {
    
    @Inject(at=@At("HEAD"), method = "setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/IBlockState;I)Z")
    public void CW$setBlockState(BlockPos pos, IBlockState state, int flags, CallbackInfoReturnable<Boolean> cir) {
        if (WoodHandler.TYPE_CACHE == null) return;
        if (((WoodObject)state).isWood() && pos.equals(WoodHandler.TYPE_CACHE.getFirst()))
            WoodTypeStorage.setWoodType(this, pos, WoodHandler.TYPE_CACHE.getSecond());
        WoodHandler.TYPE_CACHE = null;
    }
    
}
