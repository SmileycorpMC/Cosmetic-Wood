package net.smileycorp.cosmeticwood.client;

import java.awt.Color;
import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
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
		
		Minecraft mc = Minecraft.getMinecraft();
		
		World world = mc.world;
		EntityPlayer player = mc.player;
		BlockRendererDispatcher dispatcher = mc.getBlockRendererDispatcher();
		TextureAtlasSprite missing = mc.getTextureMapBlocks().getMissingSprite();
		IBlockState plankState = ((ItemBlock) plank.getItem()).getBlock().getStateForPlacement(world, new BlockPos(0,0,0), EnumFacing.UP, 0, 0, 0, plank.getMetadata(), player);
		List<BakedQuad> plank_quads = dispatcher.getModelForState(plankState).getQuads(plankState, EnumFacing.UP, 0);	
		plank_sprite = plank_quads.isEmpty() ? missing : plank_quads.get(0).getSprite();
		if (log!=null) {
			IBlockState logState = ((ItemBlock) log.getItem()).getBlock().getStateForPlacement(world, new BlockPos(0,0,0), EnumFacing.UP, 0, 0, 0, log.getMetadata(), player);
			IBakedModel logModel = dispatcher.getModelForState(logState);
			List<BakedQuad> log_top_quads = logModel.getQuads(logState, EnumFacing.UP, 0);
			log_top_sprite = log_top_quads.isEmpty() ? plank_sprite : log_top_quads.get(0).getSprite();
			List<BakedQuad> log_side_quads = logModel.getQuads(logState, EnumFacing.UP, 0);
			log_side_sprite = log_side_quads.isEmpty() ? plank_sprite : log_top_quads.get(0).getSprite();
		} else {
			log_top_sprite = plank_sprite;
			log_side_sprite = plank_sprite;
		}
		colour = new Color(plank_sprite.getFrameTextureData(0)[0][0]);
	}
	
	public ImmutableMap<String, String> getTextures() {
		return ImmutableMap.<String, String>builder().put("plank", plank_sprite.getIconName()).put("particle", plank_sprite.getIconName())
		.put("log_top", log_top_sprite.getIconName()).put("log_side", log_side_sprite.getIconName()).build();
	}
	
	public Color getColour() {
		return colour;
	}

}
