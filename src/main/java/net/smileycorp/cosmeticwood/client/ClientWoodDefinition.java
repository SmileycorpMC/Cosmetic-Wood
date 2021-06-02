package net.smileycorp.cosmeticwood.client;

import java.awt.Color;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.google.common.collect.ImmutableMap;

public class ClientWoodDefinition {
		
	private TextureAtlasSprite plank_sprite, log_top_sprite, log_side_sprite = null;
	
	private Color colour;
	
	public ClientWoodDefinition(ItemStack plank, ItemStack log) {
		World world = Minecraft.getMinecraft().world;
		EntityLivingBase entity = Minecraft.getMinecraft().player;
		
		BlockRendererDispatcher dispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
		
		IBlockState plankState = ((ItemBlock) plank.getItem()).getBlock()
				.getStateForPlacement(world, new BlockPos(0,0,0), EnumFacing.UP, 0, 0, 0, plank.getMetadata(), entity);
				
		plank_sprite = dispatcher.getModelForState(plankState).getQuads(plankState, EnumFacing.UP, 0).get(0).getSprite();
		if (log!=null) {
			IBlockState logState = ((ItemBlock) log.getItem()).getBlock()
					.getStateForPlacement(world, new BlockPos(0,0,0), EnumFacing.UP, 0, 0, 0, log.getMetadata(), entity);
			IBakedModel logModel = dispatcher.getModelForState(logState);
			
			log_top_sprite = logModel.getQuads(logState, EnumFacing.UP, 0).get(0).getSprite();
			log_side_sprite = logModel.getQuads(logState, EnumFacing.NORTH, 0).get(0).getSprite();
		} else {
			log_top_sprite = plank_sprite;
			log_side_sprite = plank_sprite;
		}
		
		colour = new Color(plank_sprite.getFrameTextureData(0)[0][0]);
		//System.out.println(colour + " " + name);
	}
	
	public ImmutableMap<String, String> getTextures() {
		return ImmutableMap.<String, String>builder().put("plank", plank_sprite.getIconName()).put("particle", plank_sprite.getIconName())
		.put("log_top", log_top_sprite.getIconName()).put("log_side", log_side_sprite.getIconName()).build();
	}
	
	public Color getColour() {
		return colour;
	}

}
