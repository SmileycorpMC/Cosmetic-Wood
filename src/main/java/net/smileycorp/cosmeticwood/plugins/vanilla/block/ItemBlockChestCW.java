package net.smileycorp.cosmeticwood.plugins.vanilla.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.smileycorp.cosmeticwood.common.WoodHandler;
import net.smileycorp.cosmeticwood.common.block.ItemBlockSimpleWood;
import net.smileycorp.cosmeticwood.plugins.vanilla.client.TESRCWChest;
import net.smileycorp.cosmeticwood.plugins.vanilla.tileentity.TileCWChest;

public class ItemBlockChestCW extends ItemBlockSimpleWood {
    
    public ItemBlockChestCW(Block block) {
        super(block);
    }
    
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
        if (!block.isReplaceable(world, pos)) pos = pos.offset(facing);
        if (stack.isEmpty() |! player.canPlayerEdit(pos, facing, stack)) return EnumActionResult.FAIL;
        int i = 0;
        if (canConnect(world, stack, pos.north())) {
            if (isDoubleChest(world, stack, pos.north())) return EnumActionResult.FAIL;
            i++;
        }
        if (canConnect(world, stack, pos.south())) {
            if (isDoubleChest(world, stack, pos.south())) return EnumActionResult.FAIL;;
            i++;
        }
        if (canConnect(world, stack, pos.east())) {
            if (isDoubleChest(world, stack, pos.east())) return EnumActionResult.FAIL;;
            i++;
        }
        if (canConnect(world, stack, pos.west())) {
            if (isDoubleChest(world, stack, pos.west())) return EnumActionResult.FAIL;;
            i++;
        }
        if (i > 1) return EnumActionResult.FAIL;
        IBlockState state = block.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, getMetadata(stack.getMetadata()), player, hand);
        if (placeBlockAt(stack, player, world, pos, facing, hitX, hitY, hitZ, state)) {
            state = world.getBlockState(pos);
            SoundType soundtype = state.getBlock().getSoundType(state, world, pos, player);
            world.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
            stack.shrink(1);
        }
        return EnumActionResult.SUCCESS;
    }
    
    private boolean isDoubleChest(World world, ItemStack stack, BlockPos pos) {
        for (EnumFacing facing : EnumFacing.Plane.HORIZONTAL) if (canConnect(world, stack, pos.offset(facing))) return true;
        return false;
    }
    
    private boolean canConnect(IBlockAccess source, ItemStack stack, BlockPos pos) {
        ResourceLocation loc = WoodHandler.getRegistry(stack);
        if (source.getBlockState(pos).getBlock() != getBlock()) return false;
        if (!(source.getTileEntity(pos) instanceof TileCWChest)) return false;
        return loc.equals(((TileCWChest)source.getTileEntity(pos)).getType());
    }
    
    @SideOnly(Side.CLIENT)
    public TileEntityItemStackRenderer getITESR() {
        return new TESRCWChest.Item();
    }
    
}
