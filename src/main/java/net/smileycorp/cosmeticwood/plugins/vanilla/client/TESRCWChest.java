package net.smileycorp.cosmeticwood.plugins.vanilla.client;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.model.ModelLargeChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.smileycorp.cosmeticwood.common.ModDefinitions;
import net.smileycorp.cosmeticwood.common.WoodHandler;
import net.smileycorp.cosmeticwood.plugins.vanilla.tileentity.TileCWChest;

import java.util.Calendar;

public class TESRCWChest extends TileEntitySpecialRenderer<TileCWChest> {
	
    private static final ResourceLocation TEXTURE_TRAPPED_DOUBLE = ModDefinitions.getResource("textures/entity/chest/trapped_double.png");
    private static final ResourceLocation TEXTURE_CHRISTMAS_DOUBLE = new ResourceLocation("textures/entity/chest/christmas_double.png");
    private static final ResourceLocation TEXTURE_NORMAL_DOUBLE = ModDefinitions.getResource("textures/entity/chest/normal_double.png");
    private static final ResourceLocation TEXTURE_TRAPPED = ModDefinitions.getResource("textures/entity/chest/trapped.png");
    private static final ResourceLocation TEXTURE_CHRISTMAS = new ResourceLocation("textures/entity/chest/christmas.png");
    private static final ResourceLocation TEXTURE_NORMAL = ModDefinitions.getResource("textures/entity/chest/normal.png");
    private final ModelChest simpleChest;
    private final ModelChest largeChest;
    private boolean isChristmas;

    public TESRCWChest() {
        Calendar calendar = Calendar.getInstance();
        if (calendar.get(2) + 1 == 12 && calendar.get(5) >= 24 && calendar.get(5) <= 26) isChristmas = true;
        simpleChest = isChristmas ? new ModelChest() : new ModelChestCW();
        largeChest = isChristmas ? new ModelLargeChest() : new ModelLargeChestCW();
    }

    @Override
	public void render(TileCWChest te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        GlStateManager.enableDepth();
        GlStateManager.depthFunc(515);
        GlStateManager.depthMask(true);
        int i;
        if (te.hasWorld()) {
            Block block = te.getBlockType();
            i = te.getBlockMetadata();
            if (block instanceof BlockChest && i == 0) {
                ((BlockChest)block).checkForSurroundingChests(te.getWorld(), te.getPos(), te.getWorld().getBlockState(te.getPos()));
                i = te.getBlockMetadata();
            }
            te.checkForAdjacentChests();
        }
        else i = 0;
        if (te.adjacentChestZNeg == null && te.adjacentChestXNeg == null) {
            ModelChest modelchest;
            if (te.adjacentChestXPos == null && te.adjacentChestZPos == null) {
                modelchest = this.simpleChest;
                if (destroyStage >= 0) {
                    bindTexture(DESTROY_STAGES[destroyStage]);
                    GlStateManager.matrixMode(5890);
                    GlStateManager.pushMatrix();
                    GlStateManager.scale(4.0F, 4.0F, 1.0F);
                    GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
                    GlStateManager.matrixMode(5888);
                }
                else if (this.isChristmas) bindTexture(TEXTURE_CHRISTMAS);
                else if (te.getChestType() == BlockChest.Type.TRAP) bindTexture(TEXTURE_TRAPPED);
                else bindTexture(TEXTURE_NORMAL);
            }
            else {
                modelchest = largeChest;
                if (destroyStage >= 0) {
                    this.bindTexture(DESTROY_STAGES[destroyStage]);
                    GlStateManager.matrixMode(5890);
                    GlStateManager.pushMatrix();
                    GlStateManager.scale(8.0F, 4.0F, 1.0F);
                    GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
                    GlStateManager.matrixMode(5888);
                }
                else if (this.isChristmas) bindTexture(TEXTURE_CHRISTMAS_DOUBLE);
                else if (te.getChestType() == BlockChest.Type.TRAP) bindTexture(TEXTURE_TRAPPED_DOUBLE);
                else bindTexture(TEXTURE_NORMAL_DOUBLE);
            }
            GlStateManager.pushMatrix();
            GlStateManager.enableRescaleNormal();
            if (destroyStage < 0) GlStateManager.color(1.0F, 1.0F, 1.0F, alpha);
            GlStateManager.translate((float)x, (float)y + 1.0F, (float)z + 1.0F);
            GlStateManager.scale(1.0F, -1.0F, -1.0F);
            GlStateManager.translate(0.5F, 0.5F, 0.5F);
            int j = 0;
            if (i == 2) j = 180;
            if (i == 3) j = 0;
            if (i == 4) j = 90;
            if (i == 5) j = -90;
            if (i == 2 && te.adjacentChestXPos != null) GlStateManager.translate(1.0F, 0.0F, 0.0F);
            if (i == 5 && te.adjacentChestZPos != null) GlStateManager.translate(0.0F, 0.0F, -1.0F);
            GlStateManager.rotate(j, 0.0F, 1.0F, 0.0F);
            GlStateManager.translate(-0.5F, -0.5F, -0.5F);
            float f = te.prevLidAngle + (te.lidAngle - te.prevLidAngle) * partialTicks;
            if (te.adjacentChestZNeg != null) {
                float f1 = te.adjacentChestZNeg.prevLidAngle + (te.adjacentChestZNeg.lidAngle - te.adjacentChestZNeg.prevLidAngle) * partialTicks;
                if (f1 > f) f = f1;
            }
            if (te.adjacentChestXNeg != null) {
                float f2 = te.adjacentChestXNeg.prevLidAngle + (te.adjacentChestXNeg.lidAngle - te.adjacentChestXNeg.prevLidAngle) * partialTicks;
                if (f2 > f) f = f2;
            }
            f = 1.0F - f;
            f = 1.0F - f * f * f;
            modelchest.chestLid.rotateAngleX = -(f * ((float)Math.PI / 2F));
            if (modelchest instanceof ColouredChest) ((ColouredChest) modelchest).setColour(WoodHandler.getColour(te.getType()));
            modelchest.renderAll();
            GlStateManager.disableRescaleNormal();
            GlStateManager.popMatrix();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            if (destroyStage >= 0) {
                GlStateManager.matrixMode(5890);
                GlStateManager.popMatrix();
                GlStateManager.matrixMode(5888);
            }
        }
    }
    
    public static class Item extends TileEntityItemStackRenderer {
    
        private final TileCWChest normal_tile = new TileCWChest();
        private final TileCWChest trapped_tile = new TileCWChest(BlockChest.Type.TRAP);
        
        public void renderByItem(ItemStack stack, float partialTicks) {
            TileCWChest tile = ((BlockChest)((ItemBlock)stack.getItem()).getBlock()).chestType == BlockChest.Type.TRAP ? trapped_tile : this.normal_tile;
            tile.setType(WoodHandler.getRegistry(stack));
            TileEntityRendererDispatcher.instance.render(tile, 0, 0, 0, 0, partialTicks);
        }
        
    }

}
