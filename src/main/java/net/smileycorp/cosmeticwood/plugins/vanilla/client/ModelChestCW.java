package net.smileycorp.cosmeticwood.plugins.vanilla.client;

import net.minecraft.client.model.ModelChest;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.*;

public class ModelChestCW extends ModelChest implements ColouredChest {
	
	private Color colour = null;
	
	@Override
	public void renderAll() {
        chestKnob.rotateAngleX = this.chestLid.rotateAngleX;
        chestKnob.render(0.0625F);
        GlStateManager.color((float)colour.getRed()/255f, (float)colour.getGreen()/255f, (float)colour.getBlue()/255f);
        chestLid.render(0.0625F);
        chestBelow.render(0.0625F);
        GlStateManager.color(1, 1, 1);
    }

	@Override
	public void setColour(Color colour) {
		this.colour = colour;
	}
	
}
