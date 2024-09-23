package net.smileycorp.cosmeticwood.mixin;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.smileycorp.cosmeticwood.api.registry.item.WoodItem;
import net.smileycorp.cosmeticwood.common.data.WoodHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityPlayer.class)
public abstract class MixinEntityPlayer extends EntityLivingBase {
    
    public MixinEntityPlayer(World world) {
        super(world);
    }
    
    @Inject(at=@At("RETURN"), method = "canPlayerEdit")
    public void CW$canPlayerEdit(BlockPos pos, EnumFacing facing, ItemStack stack, CallbackInfoReturnable<Boolean> callback) {
        if (!callback.getReturnValue() || world == null |! ((WoodItem)(Object)stack).isWood()) return;
        WoodHandler.TYPE_CACHE = new Tuple(pos, ((WoodItem)(Object)stack).getType(stack));
    }

}
