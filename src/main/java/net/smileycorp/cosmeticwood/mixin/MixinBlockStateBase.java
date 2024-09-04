package net.smileycorp.cosmeticwood.mixin;

import net.minecraft.block.state.BlockStateBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.smileycorp.cosmeticwood.api.registry.block.WoodBlock;
import net.smileycorp.cosmeticwood.api.registry.item.WoodStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlockStateBase.class)
public abstract class MixinBlockStateBase implements WoodBlock, WoodStack, IBlockState {
    
    @Override
    public boolean isWood() {
        return ((WoodBlock)getBlock()).isWood();
    }
    
    @Override
    public ResourceLocation getDefaultType() {
        return ((WoodBlock)getBlock()).getDefaultType();
    }
    
    @Override
    public String[] getModIds() {
        return ((WoodBlock)getBlock()).getModIds();
    }
    
    @Override
    public ResourceLocation getType() {
        return getType((ItemStack)(Object)this);
    }
    
    @Override
    public ResourceLocation getType(IBlockAccess world, BlockPos pos) {
        return ((WoodBlock)getBlock()).getType(world, pos);
    }
    
    @Override
    public String getInventoryVariant() {
        return ((WoodBlock)getBlock()).getInventoryVariant();
    }
    
}
