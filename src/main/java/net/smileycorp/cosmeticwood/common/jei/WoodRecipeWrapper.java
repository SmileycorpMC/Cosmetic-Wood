package net.smileycorp.cosmeticwood.common.jei;

import java.util.ArrayList;
import java.util.List;

import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.IFocus.Mode;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.wrapper.ICustomCraftingRecipeWrapper;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreIngredient;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.smileycorp.cosmeticwood.common.ModdedWoodHandler;
import net.smileycorp.cosmeticwood.common.WoodHandler;
import net.smileycorp.cosmeticwood.common.recipe.IWoodRecipe;

public class WoodRecipeWrapper implements IRecipeWrapper, ICustomCraftingRecipeWrapper {
	
	private final IWoodRecipe recipe;
	private NonNullList<ItemStack> outputs = NonNullList.<ItemStack>create();
	int height;
	int width;
	
	public WoodRecipeWrapper(IWoodRecipe recipe) {
		this.recipe=recipe;
	}
	
	@SuppressWarnings({"deprecation", "null"})
	@Override
	public void getIngredients(IIngredients ingredients) {
		List<List<ItemStack>> inputs = new ArrayList<List<ItemStack>>();
		Ingredient wood = null;
		//inputs.add(new ArrayList<ItemStack>());
		for (Ingredient ingredient : ((IRecipe)this.recipe).getIngredients()) {
			List<ItemStack> input = new ArrayList<ItemStack>();
			//System.out.println(ingredient.toString());
			for (ItemStack stack : ingredient.getMatchingStacks()) {
				if (wood==null) {
					if (WoodHandler.getName(stack)!=null) {
						wood = ingredient;
					}
				}
				input.add(stack);
			}
			inputs.add(input);
		}
		Block block = ((ItemBlock) ((IRecipe) recipe).getRecipeOutput().getItem()).getBlock();
		for (String type : ModdedWoodHandler.getTypes(block.getRegistryName().getResourceDomain())) {
			ItemStack output = ((IRecipe)recipe).getRecipeOutput();
			NBTTagCompound nbt = new NBTTagCompound();
	        nbt.setString("type", type);
	        output.setTagCompound(nbt);
	        outputs.add(output);
		}
		
		ingredients.setInputLists(ItemStack.class, inputs);
		List<List<ItemStack>> outlist = new ArrayList<List<ItemStack>>();
		outlist.add(outputs);
		ingredients.setOutputLists(ItemStack.class, outlist);
		
		//System.out.println(inputs.toString());
		//System.out.println(outputs.toString());
		
		if (recipe instanceof ShapedOreRecipe) {
			height = ((ShapedOreRecipe)recipe).getHeight();
			width = ((ShapedOreRecipe)recipe).getWidth();
		} else {
			//int size = inputs.size();
			height = 3;
			width = 3;
		}
	}

	@Override
	public void setRecipe(IRecipeLayout layout, IIngredients ingredients) {
		IGuiItemStackGroup displayStacks = layout.getItemStacks();
		IFocus<?> focus = layout.getFocus();
		List<List<ItemStack>> inputs = ingredients.getInputs(ItemStack.class);
		List<ItemStack> outputs = this.outputs;
		if (focus.getValue() instanceof ItemStack) {
			Mode focusMode = focus.getMode();
			ItemStack stack = (ItemStack)focus.getValue();
			if(focusMode == IFocus.Mode.INPUT && WoodHandler.getName(stack)!=null) {
				ItemStack output = ((IRecipe)recipe).getRecipeOutput();
				String type = WoodHandler.getName(stack);
				//System.out.println(type);
				NBTTagCompound nbt = new NBTTagCompound();
		        nbt.setString("type", type);
		        output.setTagCompound(nbt);
		        //System.out.println(nbt.toString());
		        //System.out.println(stack.toString());
				outputs.clear();
				outputs.add(output);
				inputs = changeInputs(inputs, type);
			}else if(focusMode == IFocus.Mode.OUTPUT) {
				NBTTagCompound nbt = stack.getTagCompound();
				if (nbt!=null) {
					if (nbt.hasKey("type")) {
						String type = nbt.getString("type");
						ItemStack output = ((IRecipe)recipe).getRecipeOutput();
						//System.out.println(type);
				        output.setTagCompound(nbt);
				        //System.out.println(nbt.toString());
				        //System.out.println(stack.toString());
				        outputs.clear();
				        outputs.add(output);
						inputs = changeInputs(inputs, type);
					}
				}
			}
		}
		
		JEIIntegration.craftingHelper.setInputs(displayStacks, inputs, this.width, this.height);
		//System.out.println(outputs);
		JEIIntegration.craftingHelper.setOutput(displayStacks, outputs);
	}

	private List<List<ItemStack>> changeInputs(List<List<ItemStack>> inputs, String type) {
		List<List<ItemStack>> result = new ArrayList<List<ItemStack>>();
		for (List<ItemStack> input : inputs) {
			List<ItemStack> stack = new ArrayList<ItemStack>();
			if (new OreIngredient("plankWood").apply(input.get(0))) {
				stack.add(WoodHandler.getPlankStack(type));
			} else if (new OreIngredient("logWood").apply(input.get(0))) {
				stack.add(WoodHandler.getLogStack(type));
			} else {
				stack = input;
			}
			result.add(stack);
		}
		return result;
	}

}