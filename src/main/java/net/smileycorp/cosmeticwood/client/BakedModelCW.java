package net.smileycorp.cosmeticwood.client;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.smileycorp.atlas.api.client.RenderingUtils;
import net.smileycorp.cosmeticwood.common.data.WoodHandler;
import net.smileycorp.cosmeticwood.common.data.WoodTypeStorage;

import javax.annotation.Nullable;
import java.util.List;

@SideOnly(Side.CLIENT)
public class BakedModelCW extends BakedModelWrapper<IBakedModel> {

	private final IModel base;
	private BlockPos pos;
	private IBlockAccess world;
	
	public BakedModelCW(IBakedModel baked, IModel base) {
		super(baked);
		this.base = base;
	}
	
	@Override
    public TextureAtlasSprite getParticleTexture() {
        return ClientProxy.getGreyscaleSprite("plank");
    }
	
	@Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
		try {
			ResourceLocation type = WoodHandler.getDefault();
			if (pos != null && world != null) type = WoodTypeStorage.getWoodType(world, pos);
			IModel newModel = base.retexture(WoodHandler.getInstance().getTextures(type));
			return newModel.bake(newModel.getDefaultState(), DefaultVertexFormats.BLOCK, RenderingUtils.defaultTextureGetter).getQuads(state, side, rand);
		} catch (Exception e) {
			e.printStackTrace();
			return originalModel.getQuads(state, side, rand);
		}
       
    }
	
	public void setContext(BlockPos pos, IBlockAccess world) {
		this.pos = pos;
		this.world = world;
	}
	
	@Override
    public ItemOverrideList getOverrides() {
        return new CWItemOverrides(base);
    }
	
	public static class CWItemOverrides extends ItemOverrideList {
		
		public final IModel base;
		
		public CWItemOverrides(IModel base) {
			super(ImmutableList.of());
			this.base = base;
		}
		
		@Override
		public IBakedModel handleItemState(IBakedModel base, ItemStack stack, World world, EntityLivingBase entity) {
			try {
				NBTTagCompound tag = stack.getTagCompound();
				String variant = tag.getString("type");
				IModel newModel = this.base.retexture(WoodHandler.getInstance().getTextures(WoodHandler.getInstance().fixData(variant)));
				return newModel.bake(newModel.getDefaultState(), DefaultVertexFormats.BLOCK, RenderingUtils.defaultTextureGetter);
			} catch (Exception e) {
				return base;
			}
		}
		
	}

}
