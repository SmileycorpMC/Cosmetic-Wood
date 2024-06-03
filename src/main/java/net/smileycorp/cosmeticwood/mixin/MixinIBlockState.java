package net.smileycorp.cosmeticwood.mixin;

import net.minecraft.block.state.IBlockState;
import net.smileycorp.cosmeticwood.common.item.WoodStack;
import net.smileycorp.cosmeticwood.common.block.WoodBlock;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(IBlockState.class)
public abstract class MixinIBlockState implements WoodBlock, WoodStack {

}
