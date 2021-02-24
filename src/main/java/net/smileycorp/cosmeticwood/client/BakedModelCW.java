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
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.smileycorp.atlas.api.client.RenderingUtils;
import net.smileycorp.cosmeticwood.common.WoodHandler;
import net.smileycorp.cosmeticwood.common.block.BlockCW;

@SideOnly(Side.CLIENT)
public class BakedModelCW extends BakedModelWrapper<IBakedModel> {

	private final IModel base;
	
	public BakedModelCW(IBakedModel baked, IModel base) {
		super(baked);
		this.base=base;
	}
	
	@Override
    public TextureAtlasSprite getParticleTexture() {
        return ClientProxy.GREYSCALE_PLANKS;
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
			IModel newModel = this.base.retexture(WoodHandler.getTextures(variant));
			return newModel.bake(newModel.getDefaultState(), DefaultVertexFormats.BLOCK, RenderingUtils.defaultTextureGetter).getQuads(state, side, rand);
		} catch (Exception e) {
			e.printStackTrace();
			return originalModel.getQuads(state, side, rand);
		}
       
    }	
	
	@Override
    public ItemOverrideList getOverrides() {
        return new CWItemOverrides(this, base);
    }
	
	public static class CWItemOverrides extends ItemOverrideList {
		
		public final IModel base;
		
		public CWItemOverrides(BakedModelCW baked, IModel base) {
			super(ImmutableList.of());
			this.base=base;
		}
		
		@Override
		public IBakedModel handleItemState(IBakedModel base, ItemStack stack, World world, EntityLivingBase entity) {
			try {
				NBTTagCompound tag = stack.getTagCompound();
				String variant = tag.getString("type");
				IModel newModel = this.base.retexture(WoodHandler.getTextures(variant));
				return newModel.bake(newModel.getDefaultState(), DefaultVertexFormats.BLOCK, RenderingUtils.defaultTextureGetter);
			} catch (Exception e) {
				return base;
			}
		}
		
	}

}
