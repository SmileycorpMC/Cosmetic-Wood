package net.smileycorp.cosmeticwood.integration.jei;

import com.google.common.collect.Lists;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.IFocus.Mode;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.wrapper.ICustomCraftingRecipeWrapper;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreIngredient;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.smileycorp.cosmeticwood.common.CosmeticWood;
import net.smileycorp.cosmeticwood.common.WoodDefinition;
import net.smileycorp.cosmeticwood.common.WoodHandler;
import net.smileycorp.cosmeticwood.common.block.WoodBlock;
import net.smileycorp.cosmeticwood.common.item.WoodItem;
import net.smileycorp.cosmeticwood.common.recipe.WoodRecipe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class WoodRecipeWrapper implements IRecipeWrapper, ICustomCraftingRecipeWrapper {
	
	private final WoodRecipe recipe;
	final int height;
	final int width;
	
	public WoodRecipeWrapper(WoodRecipe recipe) {
		this.recipe = recipe;
		if (recipe instanceof ShapedOreRecipe) {
			height = ((ShapedOreRecipe)recipe).getHeight();
			width = ((ShapedOreRecipe)recipe).getWidth();
		} else {
			height = 3;
			width = 3;
		}
	}
	
	@Override
	public void getIngredients(IIngredients ingredients) {
		List<List<ItemStack>> inputs = Lists.newArrayList();
		List<ItemStack> outputs = Lists.newArrayList();
		List<WoodDefinition> definitions = WoodHandler.getDefinitions(((WoodItem)recipe.getRecipeOutput().getItem()).block().getModids());
		List<ItemStack> planks = Lists.newArrayList();
		List<ItemStack> logs = Lists.newArrayList();
		for (WoodDefinition def : definitions) {
			planks.add(def.getPlankStack());
			logs.add(def.getLogStack());
			ItemStack stack = recipe.getRecipeOutput().copy();
			NBTTagCompound tag = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
			tag.setString("type", def.getRegistry().toString());
			stack.setTagCompound(tag);
			outputs.add(stack);
		}
		CosmeticWood.logInfo(outputs.stream().map(ItemStack::getTagCompound).collect(Collectors.toList()));
		ingredients.setOutputLists(VanillaTypes.ITEM, Collections.singletonList(outputs));
		for (Ingredient ingredient : recipe.getIngredients()) {
			if (ingredient.apply(new ItemStack(Blocks.PLANKS))) {
				inputs.add(Lists.newArrayList(planks));
				continue;
			}
			if (ingredient.apply(new ItemStack(Blocks.LOG))) {
				inputs.add(Lists.newArrayList(logs));
				continue;
			}
			inputs.add(Lists.newArrayList(ingredient.getMatchingStacks()));
		}
		ingredients.setInputLists(VanillaTypes.ITEM, inputs);
		for (WoodDefinition def : definitions) {
			ItemStack stack = recipe.getRecipeOutput().copy();
			NBTTagCompound tag = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
			tag.setString("type", def.getRegistry().toString());
			stack.setTagCompound(tag);
			outputs.add(stack);
		}
	}

	@Override
	public void setRecipe(IRecipeLayout layout, IIngredients ingredients) {
		IGuiItemStackGroup displayStacks = layout.getItemStacks();
		IFocus<?> focus = layout.getFocus();
		List<List<ItemStack>> inputs = ingredients.getInputs(VanillaTypes.ITEM);
		List<ItemStack> outputs = Lists.newArrayList(ingredients.getOutputs(VanillaTypes.ITEM).get(0));
		if (focus.getValue() instanceof ItemStack) {
			Mode focusMode = focus.getMode();
			ItemStack stack = (ItemStack)focus.getValue();
			if (focusMode != null && WoodHandler.getRegistry(stack) != null) {
				ItemStack output = recipe.getRecipeOutput();
				ResourceLocation type = WoodHandler.getRegistry(stack);
				if (focusMode == Mode.OUTPUT && type.equals(((WoodBlock)((ItemBlock)output.getItem()).getBlock()).getDefaultType())) {
					NBTTagCompound nbt = output.hasTagCompound() ? output.getTagCompound() : new NBTTagCompound();
					nbt.setString("type", type.toString());
					output.setTagCompound(nbt);
					outputs = Lists.newArrayList(output);
				} else if (type != null) {
					NBTTagCompound nbt = output.hasTagCompound() ? output.getTagCompound() : new NBTTagCompound();
					nbt.setString("type", type.toString());
					output.setTagCompound(nbt);
					outputs = Lists.newArrayList(output);
					inputs = changeInputs(inputs, type);
				}
			}
		}
		CosmeticWood.logInfo(outputs.stream().map(ItemStack::getTagCompound).collect(Collectors.toList()));
		JEIIntegration.craftingHelper.setInputs(displayStacks, inputs, width, height);
		JEIIntegration.craftingHelper.setOutput(displayStacks, outputs);
	}

	private List<List<ItemStack>> changeInputs(List<List<ItemStack>> inputs, ResourceLocation type) {
		List<List<ItemStack>> result = new ArrayList<>();
		for (List<ItemStack> input : inputs) {
			List<ItemStack> stack = new ArrayList<>();
			if (!input.isEmpty()) {
				if (new OreIngredient("plankWood").apply(input.get(0))) {
					stack.add(WoodHandler.getPlankStack(type));
				} else if (new OreIngredient("logWood").apply(input.get(0))) {
					stack.add(WoodHandler.getLogStack(type));
				}
			}
			if (stack.isEmpty()) stack = input;
			result.add(stack);
		}
		return result;
	}

}
