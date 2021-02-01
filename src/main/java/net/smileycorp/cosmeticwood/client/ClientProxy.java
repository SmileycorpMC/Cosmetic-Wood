package net.smileycorp.cosmeticwood.client;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.smileycorp.atlas.api.interfaces.ISidedProxy;
import net.smileycorp.cosmeticwood.common.block.BlockCW;

public class ClientProxy implements ISidedProxy {

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		
	}

	@Override
	public void init(FMLInitializationEvent event) {
		
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		
	}
	
	/*@SuppressWarnings("unchecked")
	@SubscribeEvent
	public void tooltipEvent(ItemTooltipEvent event) {
		ItemStack stack = event.getItemStack();
		if(stack.getItem() instanceof ItemBlock){
			Block block = ((ItemBlock) stack.getItem()).getBlock();
			List<String> tooltip = new ArrayList<String>(event.getToolTip());
			String name = event.getToolTip().get(0);
			tooltip.remove(0);
			event.getToolTip().clear();
			event.getToolTip().add(name);
			if (block instanceof BlockCW) {
				ItemBlock
			}
			event.getToolTip().addAll(tooltip);
		}
	}*/
}
