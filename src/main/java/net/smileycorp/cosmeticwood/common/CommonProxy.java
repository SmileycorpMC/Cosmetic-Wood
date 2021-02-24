package net.smileycorp.cosmeticwood.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang3.ArrayUtils;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistryModifiable;
import net.smileycorp.atlas.api.interfaces.ISidedProxy;
import net.smileycorp.cosmeticwood.common.block.BlockCW;
import net.smileycorp.cosmeticwood.common.block.CWBlocks;

public class CommonProxy implements ISidedProxy {
	
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		
	}

	@Override
	public void init(FMLInitializationEvent event) {
		
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		
		
	}
	
	@SubscribeEvent
	public static void recipeWriter(RegistryEvent.Register<IRecipe> event){
		IForgeRegistryModifiable recipes = (IForgeRegistryModifiable) event.getRegistry();
		recipes.remove(new ResourceLocation("minecraft", "crafting_table"));
		recipes.remove(new ResourceLocation("minecraft", "wooden_pressure_plate"));
	}
}
