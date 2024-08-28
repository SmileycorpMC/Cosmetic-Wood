package net.smileycorp.cosmeticwood.common.data;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.smileycorp.cosmeticwood.common.network.PacketHandler;
import net.smileycorp.cosmeticwood.common.network.SyncWoodTypesMessage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public interface WoodTypeStorage {
    
    @CapabilityInject(WoodTypeStorage.class)
    Capability<WoodTypeStorage> CAPABILITY = null;

    ResourceLocation getWoodType(BlockPos pos);
    
    void setWoodType(BlockPos pos, ResourceLocation type);
    
    void load(NBTTagCompound nbt);
    
    NBTTagCompound save();
    
    static ResourceLocation getWoodType(IBlockAccess world, BlockPos pos) {
        Chunk chunk = getChunk(world, pos);
        return (chunk != null && chunk.hasCapability(CAPABILITY, null)) ?
                chunk.getCapability(CAPABILITY, null).getWoodType(pos) :
                WoodHandler.getDefault();
    }
    
    static void setWoodType(IBlockAccess world, BlockPos pos, ResourceLocation type) {
        Chunk chunk = getChunk(world, pos);
        if (chunk == null) return;
        if (!chunk.hasCapability(CAPABILITY, null)) return;
        chunk.getCapability(CAPABILITY, null).setWoodType(pos, type);
        if (world instanceof WorldServer) {
            PacketHandler.CHANNEL.sendToAllTracking(new SyncWoodTypesMessage(chunk),
                    new NetworkRegistry.TargetPoint(((WorldServer) world).provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 200));
        }
    }
    
    static Chunk getChunk(IBlockAccess world, BlockPos pos) {
        if (world instanceof World) return ((World) world).getChunkFromBlockCoords(pos);
        if (world instanceof ChunkCache) {
            ChunkCache cache = (ChunkCache) world;
            int i = (pos.getX() >> 4) - cache.chunkX;
            int j = (pos.getZ() >> 4) - cache.chunkZ;
            if (cache.withinBounds(pos.getX(), pos.getZ())) return cache.chunkArray[i][j];
        }
        return null;
    }
    
    class Impl implements WoodTypeStorage {
        
        private final Map<BlockPos, ResourceLocation> cache = Maps.newHashMap();
        
        @Override
        public ResourceLocation getWoodType(BlockPos pos) {
            pos = getChunkPos(pos);
            ResourceLocation type = cache.get(pos);
            return type == null ? WoodHandler.getDefault() : cache.get(pos);
        }
        
        @Override
        public void setWoodType(BlockPos pos, ResourceLocation type) {
            pos = getChunkPos(pos);
            if (type == null) cache.remove(pos);
            else cache.put(pos, type);
        }
        
        private BlockPos getChunkPos(BlockPos pos) {
            return new BlockPos(pos.getX() & 15, pos.getY(), pos.getZ() & 15);
        }
        
        @Override
        public void load(NBTTagCompound nbt) {
            Map<Integer, ResourceLocation> types = Maps.newHashMap();
            if (nbt.hasKey("key")) for (NBTBase entry : nbt.getTagList("key", 10)) {
                NBTTagCompound compound = (NBTTagCompound) entry;
                types.put(compound.getInteger("index"), new ResourceLocation(compound.getString("type")));
            }
            if (nbt.hasKey("storage")) for (NBTBase entry : nbt.getTagList("storage", 10)) {
                NBTTagCompound compound = (NBTTagCompound) entry;
                cache.put(NBTUtil.getPosFromTag(compound.getCompoundTag("pos")), types.get(compound.getInteger("type")));
            }
        }
        
        @Override
        public NBTTagCompound save() {
            NBTTagCompound nbt = new NBTTagCompound();
            NBTTagList storage = new NBTTagList();
            List<ResourceLocation> types = Lists.newArrayList();
            for (Map.Entry<BlockPos, ResourceLocation> entry : cache.entrySet()) {
                NBTTagCompound compound = new NBTTagCompound();
                compound.setTag("pos", NBTUtil.createPosTag(entry.getKey()));
                ResourceLocation type = entry.getValue();
                int i = types.indexOf(type);
                if (i < 0) {
                    i = types.size();
                    types.add(type);
                }
                compound.setInteger("type", i);
                storage.appendTag(compound);
            }
            nbt.setTag("storage", storage);
            NBTTagList key = new NBTTagList();
            for (int i = 0; i < types.size(); i++) {
                NBTTagCompound compound = new NBTTagCompound();
                compound.setInteger("index", i);
                compound.setString("type", types.get(i).toString());
                key.appendTag(compound);
            }
            nbt.setTag("key", key);
            return nbt;
        }
        
    }
    
    class Storage implements Capability.IStorage<WoodTypeStorage> {
        
        @Nullable
        @Override
        public NBTBase writeNBT(Capability<WoodTypeStorage> capability, WoodTypeStorage instance, EnumFacing side) {
            return instance.save();
        }
        
        @Override
        public void readNBT(Capability<WoodTypeStorage> capability, WoodTypeStorage instance, EnumFacing side, NBTBase nbt) {
            instance.load((NBTTagCompound) nbt);
        }
        
    }
    
    class Provider implements ICapabilitySerializable<NBTTagCompound> {
        
        private final WoodTypeStorage impl = new Impl();
        
        @Override
        public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
            return capability == CAPABILITY;
        }
        
        @Nullable
        @Override
        public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
            return capability == CAPABILITY ? CAPABILITY.cast(impl) : null;
        }
        
        @Override
        public NBTTagCompound serializeNBT() {
            return impl.save();
        }
        
        @Override
        public void deserializeNBT(NBTTagCompound nbt) {
            impl.load(nbt);
        }
        
    }

}
