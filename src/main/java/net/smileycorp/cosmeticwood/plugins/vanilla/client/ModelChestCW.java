package net.smileycorp.cosmeticwood.plugins.vanilla.client;

import net.minecraft.client.model.ModelChest;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.*;

public class ModelChestCW extends ModelChest implements IColouredChest {
	
	private Color colour = null;
	
	@Override
	public void renderAll() {
        this.chestKnob.rotateAngleX = this.chestLid.rotateAngleX;
        this.chestKnob.render(0.0625F);
        GlStateManager.color(colour.getRed(), colour.getGreen(), colour.getBlue());
        this.chestLid.render(0.0625F);
        this.chestBelow.render(0.0625F);
        GlStateManager.color(1, 1, 1);
    }

	@Override
	public void setColour(Color colour) {
		this.colour=colour;
	}

	@Override
	public Color getColour() {
		return colour;
	}
	
}
