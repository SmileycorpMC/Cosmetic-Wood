package net.smileycorp.cosmeticwood.common.block;

import net.minecraft.block.BlockWorkbench;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.smileycorp.cosmeticwood.common.WoodHandler;

public class BlockCWWorkbench extends BlockCW {
	
	public BlockCWWorkbench() {
		super(Blocks.CRAFTING_TABLE);
		this.setRegistryName("minecraft", "crafting_table");
		this.setUnlocalizedName("workbench");
	}
	
	//directly taken from the crafting table block
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote)
        {
            return true;
        }
        else
        {
            playerIn.displayGui(new BlockWorkbench.InterfaceCraftingTable(worldIn, pos));
            playerIn.addStat(StatList.CRAFTING_TABLE_INTERACTION);
            return true;
        }
    }
}
