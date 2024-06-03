package net.smileycorp.cosmeticwood.common.tile;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.smileycorp.cosmeticwood.common.WoodHandler;

public interface WoodTile {
	
	@CapabilityInject(WoodTile.class)
	Capability<WoodTile> CAPABILITY = null;
	
	WoodTile DUMMY = new WoodTile() {
		@Override
		public ResourceLocation getType() {
			return WoodHandler.getDefault();
		}
		
		@Override
		public void setType(ResourceLocation type) {}
		
		@Override
		public String getRegistryName() {
			return "";
		}
	};
	
	ResourceLocation getType();
	
	default String getTypeString() {
		ResourceLocation type = getType();
		return (WoodHandler.isValidType(type) ? type : WoodHandler.getDefault()).toString();
	}
	
	void setType(ResourceLocation type);
	
	String getRegistryName();
	
	static boolean isWoodTile(TileEntity tile) {
		return tile instanceof WoodTile || tile.hasCapability(CAPABILITY, null);
	}
	
	static WoodTile get(TileEntity tile) {
		if (tile instanceof WoodTile) return (WoodTile) tile;
		if (tile.hasCapability(CAPABILITY, null)) return tile.getCapability(CAPABILITY, null);
		return DUMMY;
	}
	
	class Impl implements WoodTile {
		
		private ResourceLocation type = WoodHandler.getDefault();
		
		@Override
		public ResourceLocation getType() {
			return type;
		}
		
		@Override
		public void setType(ResourceLocation type) {
			this.type = type;
		}
		
		@Override
		public String getRegistryName() {
			return "";
		}
		
	}
	
	class Storage implements Capability.IStorage<WoodTile> {
		
		@Override
		public NBTBase writeNBT(Capability<WoodTile> capability, WoodTile instance, EnumFacing side) {
			return new NBTTagString(instance.getTypeString());
		}
		
		@Override
		public void readNBT(Capability<WoodTile> capability, WoodTile instance, EnumFacing side, NBTBase nbt) {
			instance.setType();
		}
		
	}
	
	class Provider implements ICapabilityProvider {
		
		protected final WoodTile instance = new Impl();
		
		@Override
		public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
			return capability == CAPABILITY;
		}
		
		@Override
		public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
			return capability == CAPABILITY ? CAPABILITY.cast(instance) : null;
		}
		
	}
	
}
