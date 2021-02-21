package net.smileycorp.cosmeticwood.client;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.smileycorp.atlas.api.client.RenderingUtils;
import net.smileycorp.cosmeticwood.common.WoodHandler;
import net.smileycorp.cosmeticwood.common.block.BlockCW;


public class BakedModelCW extends BakedModelWrapper<IBakedModel> {

	private final IModel base;
	
	public BakedModelCW(IBakedModel baked, IModel base) {
		super(baked);
		this.base=base;
	}
	
	@Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
		try {
			String variant = "oak";
			World world = Minecraft.getMinecraft().world;
			EntityLivingBase entity = Minecraft.getMinecraft().player;
			if(state instanceof IExtendedBlockState) {
				if(((IExtendedBlockState) state).getUnlistedNames().contains(BlockCW.VARIANT)) {
					variant = ((IExtendedBlockState)state).getValue(BlockCW.VARIANT);
			    }
			}
			return getModel(variant, world, entity).getQuads(state, side, rand);
		} catch (Exception e) {
			e.printStackTrace();
			return originalModel.getQuads(state, side, rand);
		}
       
    }
	
	public IBakedModel getModel(String variant, World world, EntityLivingBase entity) {
		BlockRendererDispatcher dispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
		ItemStack plankStack = WoodHandler.getPlankStack(variant);
		IBlockState plank = ((ItemBlock) plankStack.getItem()).getBlock()
				.getStateForPlacement(world, new BlockPos(0,0,0), EnumFacing.UP, 0, 0, 0, plankStack.getMetadata(), entity);
		IBakedModel plankModel = dispatcher.getModelForState(plank);
		TextureAtlasSprite plankSprite = plankModel.getQuads(plank, EnumFacing.UP, 0).get(0).getSprite();
		
		ItemStack logStack = WoodHandler.getLogStack(variant);
		IBlockState log = ((ItemBlock) logStack.getItem()).getBlock()
				.getStateForPlacement(world, new BlockPos(0,0,0), EnumFacing.UP, 0, 0, 0, logStack.getMetadata(), entity);
		IBakedModel logModel = dispatcher.getModelForState(plank);
		TextureAtlasSprite logTopSprite = plankModel.getQuads(plank, EnumFacing.UP, 0).get(0).getSprite();
		TextureAtlasSprite logSideSprite = plankModel.getQuads(plank, EnumFacing.NORTH, 0).get(0).getSprite();
		
		ImmutableMap<String, String> textures = ImmutableMap.<String, String>builder().put("plank", plankSprite.getIconName()).put("particle", plankSprite.getIconName())
				.put("log_top", logTopSprite.getIconName()).put("log_side", logSideSprite.getIconName()).build();
		IModel newModel = this.base.retexture(textures);
		return newModel.bake(newModel.getDefaultState(), DefaultVertexFormats.BLOCK, RenderingUtils.defaultTextureGetter);
	}
	
	
	@Override
    public ItemOverrideList getOverrides() {
        return new CWItemOverrides(this, base);
    }
	
	public static class CWItemOverrides extends ItemOverrideList {
		
		public final BakedModelCW baked;
		public final IModel base;
		
		public CWItemOverrides(BakedModelCW baked, IModel base) {
			super(ImmutableList.of());
			this.baked=baked;
			this.base=base;
		}
		
		@Override
		public IBakedModel handleItemState(IBakedModel base, ItemStack stack, World world, EntityLivingBase entity) {
			try {
				NBTTagCompound tag = stack.getTagCompound();
				String variant = tag.getString("type");
				return baked.getModel(variant, world, entity);
			} catch (Exception e) {
				return base;
			}
		}
		
	}

}
