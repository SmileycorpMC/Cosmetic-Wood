package net.smileycorp.cosmeticwood.common;

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
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.google.common.collect.ImmutableMap;

public class WoodDefinition {
	
	private final String name;
	private final ItemStack plank;
	private final ItemStack log;
	
	@SideOnly(Side.CLIENT)
	private TextureAtlasSprite plank_sprite, log_top_sprite, log_side_sprite = null;
	
	@SideOnly(Side.CLIENT)
	private Color colour;
	
	public WoodDefinition(String name, ItemStack plank, ItemStack log) {
		this.name=name;
		this.plank=plank;
		this.log=log;
		
		if (FMLCommonHandler.instance().getSide()==Side.CLIENT) {
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
	}
	
	public String getName() {
		return name;
	}
	
	public ItemStack getPlankStack() {
		return plank;
	}
	
	public ItemStack getLogStack() {
		return log;
	}
	
	@SideOnly(Side.CLIENT)
	public ImmutableMap<String, String> getTextures() {
		return ImmutableMap.<String, String>builder().put("plank", plank_sprite.getIconName()).put("particle", plank_sprite.getIconName())
		.put("log_top", log_top_sprite.getIconName()).put("log_side", log_side_sprite.getIconName()).build();
	}
	
	@SideOnly(Side.CLIENT)
	public Color getColour() {
		return colour;
	}
}
