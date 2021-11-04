package net.smileycorp.cosmeticwood.common.recipe;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.smileycorp.cosmeticwood.common.ContentRegistry;
import net.smileycorp.cosmeticwood.common.WoodHandler;

public interface IWoodRecipe {
	
	public Map<ResourceLocation, ItemStack> CACHE = new HashMap<ResourceLocation, ItemStack>();
	
	public default ItemStack getCraftingResult(InventoryCrafting matrix, ItemStack result) {
		ResourceLocation name = null;
		for (int i = 0; i < matrix.getSizeInventory(); i++) {
			ItemStack stack = matrix.getStackInSlot(i);
			ResourceLocation wood = WoodHandler.getRegistry(stack);
			if (ContentRegistry.ITEMS.contains(stack.getItem())) {
				NBTTagCompound nbt = stack.getTagCompound();
				if (nbt!=null) {
					if (nbt.hasKey("type")) {
						wood = WoodHandler.fixData(nbt.getString("type"));
					}
				}	
			}
			if (wood != null) {
				if (name == null) {
					name = wood;
				}  else if (!(name.equals(wood))) {
					return new ItemStack(Blocks.AIR);
				}
			}
		}
		if (CACHE.containsKey(name)) return CACHE.get(name);
		else {
			if (name!=null) {
				NBTTagCompound tag = new NBTTagCompound();
				tag.setString("type", name.toString());
				result.setTagCompound(tag);
				for (IRecipe recipe : ForgeRegistries.RECIPES) {
		            if (!(recipe instanceof IWoodRecipe)) {
		            	if (recipe.matches(matrix, null)) {
		            		result = recipe.getCraftingResult(matrix);
		            	}
		            }
		        }
				CACHE.put(name, result);
			}
			return result;
		}
	}
}

