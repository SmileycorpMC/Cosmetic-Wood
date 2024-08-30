package net.smileycorp.cosmeticwood.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.smileycorp.cosmeticwood.api.registry.block.WoodBlock;
import net.smileycorp.cosmeticwood.api.registry.item.WoodItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = ForgeHooks.class, remap = false)
public class MixinForgeHooks {
    
    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;getPickBlock(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/util/math/RayTraceResult;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/player/EntityPlayer;)Lnet/minecraft/item/ItemStack;"), method = "onPickBlock")
    private static ItemStack pickBlock(Block block, IBlockState state, RayTraceResult result, World world, BlockPos pos, EntityPlayer player) {
        ItemStack stack = block.getPickBlock(state, result, world, pos, player);
        if (((WoodBlock)block).isWood()) {
            ResourceLocation type = ((WoodBlock)block).getType(world, pos);
            if (!type.equals(((WoodBlock) block).getDefaultType())) stack = WoodItem.getStack(stack, type);
        }
        return stack;
    }
    
}
