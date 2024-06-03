package net.smileycorp.cosmeticwood.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.smileycorp.cosmeticwood.common.WoodHandler;
import net.smileycorp.cosmeticwood.common.block.ModifiableWoodBlock;
import net.smileycorp.cosmeticwood.common.block.WoodBlock;
import net.smileycorp.cosmeticwood.common.tile.TileSimpleWood;
import net.smileycorp.cosmeticwood.common.tile.WoodTile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.function.Supplier;

@Mixin(Block.class)
public class MixinBlock implements ModifiableWoodBlock {
    
    private boolean isWood;
    private ResourceLocation defaultType = WoodHandler.getDefault();
    private Supplier<TileEntity> teSupplier = () -> new TileSimpleWood();
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
    public TileEntity createNewTileEntity() {
        return teSupplier.get();
    }
    
    @Override
    public String[] getModIds() {
        return modIds;
    }
    
    @Override
    public ResourceLocation getType(IBlockAccess world, BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);
        return te instanceof WoodTile ? ((WoodTile) te).getType() : getDefaultType();
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
    public void setTileEntity(Supplier<TileEntity> supplier) {
        teSupplier = supplier;
    }
    
    @Override
    public void setModIds(String... modids) {
        this.modIds = modids;
    }
    
    @Inject(method = "getExtendedState", at = @At("TAIL"), remap = false, cancellable = true)
    public void getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos, CallbackInfoReturnable<IBlockState> callback) {
        if (!isWood()) return;
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof WoodTile) callback.setReturnValue(((IExtendedBlockState)callback.getReturnValue()).withProperty(WoodBlock.VARIANT, getDefaultType().toString()));
    }
    
    @Inject(method = "hasTileEntity(Lnet/minecraft/block/state/IBlockState;)Z", at = @At("HEAD"), remap = false, cancellable = true)
    public void hasTileEntity(IBlockState state, CallbackInfoReturnable<Boolean> callback) {
        if (isWood()) callback.setReturnValue(true);
    }
    
    @Inject(method = "createTileEntity", at = @At("HEAD"), remap = false, cancellable = true)
    public void createTileEntity(World world, IBlockState state, CallbackInfoReturnable<TileEntity> callback) {
        if (isWood()) callback.setReturnValue(createNewTileEntity());
    }
    
}
