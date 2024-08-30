package net.smileycorp.cosmeticwood.common;

import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.smileycorp.cosmeticwood.api.registry.item.WoodItem;
import net.smileycorp.cosmeticwood.common.data.WoodHandler;
import net.smileycorp.cosmeticwood.common.registry.ContentRegistry;

import java.util.Random;

public class CWCreativeTab extends CreativeTabs {
    
    private final Random rand = new Random();
    private ItemStack stack;
    private NonNullList<ItemStack> items;
    private boolean needsRefresh = true;
    
    public CWCreativeTab() {
        super(Constants.name("tab"));
        setBackgroundImageName("item_search.png");
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void displayAllRelevantItems(NonNullList<ItemStack> stacks) {
        for (Item item : ContentRegistry.ITEMS) for (ResourceLocation type :
                WoodHandler.getInstance().getTypes(((WoodItem)item).getDefaultType(), ((WoodItem)item).getModIds()))
            stacks.add(WoodItem.getStack(item, type));
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public ItemStack getIconItemStack() {
        if (items == null) {
            items = NonNullList.create();
            displayAllRelevantItems(items);
        }
        if (items.isEmpty()) return new ItemStack(Blocks.CRAFTING_TABLE);
        if (!needsRefresh && Minecraft.getMinecraft().world.getTotalWorldTime() % 80 == 1) needsRefresh = true;
        if (stack == null || (needsRefresh && Minecraft.getMinecraft().world.getTotalWorldTime() % 80 == 0)) {
            stack = items.get(rand.nextInt(items.size()));
            needsRefresh = false;
        }
        return stack;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public ItemStack getTabIconItem() {
        return getIconItemStack();
    }
    
    @Override
    public boolean hasSearchBar()
    {
        return true;
    }
    
}
