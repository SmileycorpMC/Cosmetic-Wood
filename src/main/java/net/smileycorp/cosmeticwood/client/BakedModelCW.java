package net.smileycorp.cosmeticwood.client;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
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
import net.minecraft.world.World;
import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.smileycorp.atlas.api.client.RenderingUtils;
import net.smileycorp.cosmeticwood.api.registry.block.WoodBlock;
import net.smileycorp.cosmeticwood.api.registry.item.WoodStack;
import net.smileycorp.cosmeticwood.common.data.WoodHandler;
import net.smileycorp.cosmeticwood.common.data.WoodTypeStorage;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class BakedModelCW extends BakedModelWrapper<IBakedModel> {

	private final IModel base;
	private final Map<ResourceLocation, IModel> submodels;
	private BlockPos pos;
	
	public BakedModelCW(IBakedModel baked, IModel base, Map<ResourceLocation, IModel> submodels) {
		super(baked);
		this.base = base;
		this.submodels = submodels;
	}
	
	@Override
    public TextureAtlasSprite getParticleTexture() {
        return CWModelLoader.getGreyscaleSprite("plank");
    }
	
	@Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
		try {
			ResourceLocation type = WoodHandler.getDefault();
			World world = Minecraft.getMinecraft().world;
			if (pos != null && world != null) type = WoodTypeStorage.getWoodType(world, pos);
			if (type.equals(((WoodBlock)state).getDefaultType())) {
				return originalModel.getQuads(state, side, rand);
			}
			IModel newModel = submodels.containsKey(type) ? submodels.get(type) :
					base.retexture(WoodHandler.getInstance().getTextures(type));
			return newModel.bake(newModel.getDefaultState(), DefaultVertexFormats.BLOCK, RenderingUtils.defaultTextureGetter).getQuads(state, side, rand);
		} catch (Exception e) {
			e.printStackTrace();
			return originalModel.getQuads(state, side, rand);
		}
       
    }
	
	public void setContext(BlockPos pos) {
		this.pos = pos;
	}
	
	@Override
    public ItemOverrideList getOverrides() {
        return new CWItemOverrides(base, originalModel);
    }
	
	public static class CWItemOverrides extends ItemOverrideList {
		
		protected final IModel base;
		protected final IBakedModel original;
		
		public CWItemOverrides(IModel base, IBakedModel original) {
			super(ImmutableList.of());
			this.base = base;
			this.original = original;
		}
		
		@Override
		public IBakedModel handleItemState(IBakedModel base, ItemStack stack, World world, EntityLivingBase entity) {
			try {
				NBTTagCompound tag = stack.getTagCompound();
				ResourceLocation variant = WoodHandler.getInstance().fixData(tag.getString("type"));
				if (variant == ((WoodStack)(Object)stack).getType()) return original;
				IModel newModel = this.base.retexture(WoodHandler.getInstance().getTextures(variant));
				return newModel.bake(newModel.getDefaultState(), DefaultVertexFormats.BLOCK, RenderingUtils.defaultTextureGetter);
			} catch (Exception e) {
				return original;
			}
		}
		
	}

}
