package net.smileycorp.cosmeticwood.mixin;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.smileycorp.cosmeticwood.api.registry.block.DummyWoodBlock;
import net.smileycorp.cosmeticwood.api.registry.block.WoodBlock;
import net.smileycorp.cosmeticwood.api.registry.item.WoodStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(TileEntity.class)
public abstract class MixinTileEntity implements WoodStack, WoodBlock {

    @Shadow protected World world;

    @Shadow protected BlockPos pos;

    @Override
    public boolean isWood() {
        return getBlock().isWood();
    }

    @Override
    public ResourceLocation getDefaultType() {
        return getBlock().getDefaultType();
    }

    @Override
    public String[] getModIds() {
        return getBlock().getModIds();
    }

    @Override
    public ResourceLocation getType() {
        return getBlock().getType(world, pos);
    }

    @Override
    public ResourceLocation getType(IBlockAccess world, BlockPos pos) {
        return getBlock().getType(world, pos);
    }

    private WoodBlock getBlock() {
        return world == null || pos == null ? DummyWoodBlock.INSTANCE : (WoodBlock) world.getBlockState(pos).getBlock();
    }

}
