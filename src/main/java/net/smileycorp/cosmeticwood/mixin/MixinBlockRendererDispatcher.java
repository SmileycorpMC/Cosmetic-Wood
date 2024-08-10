package net.smileycorp.cosmeticwood.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.smileycorp.cosmeticwood.common.WoodTypeStorage;
import net.smileycorp.cosmeticwood.common.block.WoodBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BlockRendererDispatcher.class)
public class MixinBlockRendererDispatcher {
    
    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;getExtendedState(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;"), method = "renderBlock")
    public IBlockState CW$renderBlock(Block instance, IBlockState state, IBlockAccess world, BlockPos pos) {
        IBlockState estate = instance.getExtendedState(state, world, pos);
        if (((WoodBlock)instance).isWood()) estate =
                ((IExtendedBlockState)estate).withProperty(WoodBlock.VARIANT, WoodTypeStorage.getWoodType(world, pos).toString());
        return estate;
    }
    
    
}
