package net.smileycorp.cosmeticwood.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.smileycorp.cosmeticwood.common.data.WoodHandler;
import net.smileycorp.cosmeticwood.common.data.WoodTypeStorage;
import net.smileycorp.cosmeticwood.common.registry.block.ModifiableWoodBlock;
import net.smileycorp.cosmeticwood.common.registry.block.WoodBlock;
import net.smileycorp.cosmeticwood.common.registry.item.WoodItem;
import net.smileycorp.cosmeticwood.common.registry.item.WoodStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public abstract class MixinBlock implements ModifiableWoodBlock {
    
    private boolean isWood;
    private ResourceLocation defaultType = WoodHandler.getDefault();
    private String[] modIds = new String[0];
    
    @Override
    public boolean isWood() {
        return isWood;
    }
    
    @Override
    public ResourceLocation getDefaultType() {
        return defaultType;
    }
    
    @Override
    public String[] getModIds() {
        return modIds;
    }
    
    @Override
    public ResourceLocation getType(IBlockAccess world, BlockPos pos) {
        return WoodTypeStorage.getWoodType(world, pos);
    }
    
    @Override
    public void setWoodBlock() {
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
    
    @Inject(at = @At("HEAD"), method = "onBlockPlacedBy")
    public void CW$onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack, CallbackInfo callback) {
        if (!isWood()) return;
        WoodTypeStorage.setWoodType(world, pos, ((WoodStack)(Object)stack).getType());
    }
    
    @Inject(at = @At("HEAD"), method = "getBlockLayer", cancellable = true)
    public void CW$getBlockLayer(CallbackInfoReturnable<BlockRenderLayer> callback) {
        if (isWood()) callback.setReturnValue(BlockRenderLayer.CUTOUT_MIPPED);
    }
    
    @Inject(at = @At("RETURN"), method = "getDrops(Lnet/minecraft/util/NonNullList;Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/IBlockState;I)V", remap = false)
    public void CW$getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune, CallbackInfo callback) {
        if (!((WoodBlock)state).isWood()) return;
        for (int i = 0; i < drops.size(); i++) {
            ItemStack stack = drops.get(i);
            if (!((WoodStack)(Object)stack).isWoodItem()) continue;
            if (ItemBlock.getItemFromBlock(state.getBlock()) != stack.getItem()) continue;
            drops.set(i, WoodItem.getStack(stack, WoodTypeStorage.getWoodType(world, pos)));
        }
    }
    
}
