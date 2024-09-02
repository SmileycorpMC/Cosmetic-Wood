package net.smileycorp.cosmeticwood.client;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.block.model.multipart.Multipart;
import net.minecraft.client.renderer.block.model.multipart.Selector;
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
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.smileycorp.atlas.api.client.RenderingUtils;
import net.smileycorp.cosmeticwood.api.registry.block.WoodBlock;
import net.smileycorp.cosmeticwood.api.registry.item.WoodStack;
import net.smileycorp.cosmeticwood.common.CWLogger;
import net.smileycorp.cosmeticwood.common.data.WoodDefinition;
import net.smileycorp.cosmeticwood.common.data.WoodHandler;
import net.smileycorp.cosmeticwood.common.data.WoodTypeStorage;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class BakedModelCW extends BakedModelWrapper<IBakedModel> {

	private final ModelResourceLocation base;
	private final Map<ResourceLocation, IModel> submodels;
	private BlockPos pos;
	
	public BakedModelCW(IBakedModel baked, ModelResourceLocation base, Map<ResourceLocation, IModel> submodels) {
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
			if (type.equals(((WoodBlock)state).getDefaultType())) return originalModel.getQuads(state, side, rand);
			try {
				IModel newModel = submodels.containsKey(type) ? submodels.get(type) :
						ModelLoaderRegistry.getModel(base).retexture(WoodHandler.getInstance().getTextures(type));
				return newModel.bake(newModel.getDefaultState(), DefaultVertexFormats.BLOCK, RenderingUtils.defaultTextureGetter).getQuads(state, side, rand);
			} catch (Exception e) {
				CWLogger.logInfo("Loading multipart model for " + base);
				WoodDefinition wood = WoodHandler.getInstance().get(type);
				ModelLoader bakery = ModelLoader.VanillaLoader.INSTANCE.getLoader();
				ModelBlockDefinition def = bakery.getModelBlockDefinition(base);
				Multipart multipart = def.getMultipartData();
				if (multipart == null) CWLogger.logInfo("fucking shit");
				List<BakedQuad> quads = Lists.newArrayList();
				for (Selector selector : multipart.getSelectors()) {
					quads.addAll(getQuads(bakery, selector, wood, state, side, rand));
				}
				return quads;
			}
		} catch (Exception e) {
			CWLogger.logError("Error baking model " + base, e);
			return originalModel.getQuads(state, side, rand);
		}
    }
	
	private Collection<? extends BakedQuad> getQuads(ModelLoader bakery, Selector selector, WoodDefinition def, IBlockState state, EnumFacing side, long rand) {
		WeightedBakedModel.Builder weighted = new WeightedBakedModel.Builder();
		ImmutableMap<String, String> textures = def.getTextures();
		for (Variant variant : selector.getVariantList().getVariantList()) {
			CWLogger.logInfo(variant.getModelLocation());
			ModelBlock modelblock = bakery.models.get(variant.getModelLocation());
			if (modelblock == null) continue;
			CWLogger.logInfo("a");
			if (!modelblock.isResolved()) continue;
			CWLogger.logInfo("b");
			if (modelblock.getElements().isEmpty()) continue;
			CWLogger.logInfo("c");
			TextureAtlasSprite particle = RenderingUtils.defaultTextureGetter.apply(new ResourceLocation(textures.get("plank")));
			SimpleBakedModel.Builder builder = (new SimpleBakedModel.Builder(modelblock, modelblock.createOverrides())).setTexture(particle);
			ModelRotation rotation = variant.getRotation();
			for (BlockPart blockpart : modelblock.getElements()) {
				BlockPartFace blockpartface = blockpart.mapFaces.get(side);
				TextureAtlasSprite sprite = RenderingUtils.defaultTextureGetter.apply(new ResourceLocation(textures.containsKey(blockpartface.texture) ?
						textures.get(blockpartface.texture) :  modelblock.resolveTextureName(blockpartface.texture)));
				if (blockpartface.cullFace == null || !TRSRTransformation.isInteger(rotation.getMatrix()))
					builder.addGeneralQuad(bakery.makeBakedQuad(blockpart, blockpartface, sprite, side, rotation, variant.isUvLock()));
				else
					builder.addFaceQuad(rotation.rotate(blockpartface.cullFace), bakery.makeBakedQuad(blockpart, blockpartface, sprite, side, rotation, variant.isUvLock()));
			}
			CWLogger.logInfo(builder.makeBakedModel());
			weighted.add(builder.makeBakedModel(), variant.getWeight());
		}
		return weighted.build().getQuads(state, side, rand);
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
		
		public CWItemOverrides(ModelResourceLocation base, IBakedModel original) {
            super(ImmutableList.of());
            this.base = ModelLoaderRegistry.getModelOrMissing(base);
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
