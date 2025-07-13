package net.smileycorp.cosmeticwood.api.registry.block;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class DummyWoodBlock implements WoodBlock {

    public static final WoodBlock INSTANCE = new DummyWoodBlock();

    @Override
    public ResourceLocation getType(IBlockAccess world, BlockPos pos) {
        return null;
    }

    @Override
    public boolean isWood() {
        return false;
    }

    @Override
    public ResourceLocation getDefaultType() {
        return null;
    }

    @Override
    public String[] getModIds() {
        return new String[0];
    }

}
