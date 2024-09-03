package net.smileycorp.cosmeticwood.client;

import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.BlockStateMapper;
import net.smileycorp.cosmeticwood.common.Constants;

import java.util.Map;

public class CWStateMapperWrapper extends BlockStateMapper {
    
    private final BlockStateMapper wrapped;
    
    public CWStateMapperWrapper(BlockStateMapper wrapped) {
        this.wrapped = wrapped;
    }
    
    public Map<IBlockState, ModelResourceLocation> getVariants(Block block) {
        Map<IBlockState, ModelResourceLocation> variants = Maps.newHashMap();
        for (Map.Entry<IBlockState, ModelResourceLocation> entry : wrapped.getVariants(block).entrySet()) {
            ModelResourceLocation value = entry.getValue();
            variants.put(entry.getKey(), new ModelResourceLocation(Constants.loc(value.getResourceDomain() +
                    "/" + value.getResourcePath()), value.getVariant()));
        }
        return variants;
    }
    
}
