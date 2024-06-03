package net.smileycorp.cosmeticwood.mixin;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.state.BlockStateBase;
import net.minecraft.block.state.IBlockState;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.smileycorp.cosmeticwood.common.ContentRegistry;
import net.smileycorp.cosmeticwood.common.block.WoodBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Optional;

@Mixin(value = ExtendedBlockState.class, remap = false)
public abstract class MixinExtendedBlockState {
    
    @Inject(method = "buildUnlistedMap" ,at = @At("RETURN"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private static void buildUnlistedMap(IUnlistedProperty<?>[] unlistedProperties, CallbackInfoReturnable<ImmutableMap<IUnlistedProperty<?>, Optional<?>>> cir, ImmutableMap.Builder builder) {
        builder.put(WoodBlock.VARIANT, Optional.empty());
    }
    
}
