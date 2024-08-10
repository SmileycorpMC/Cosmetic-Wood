package net.smileycorp.cosmeticwood.api;

import com.google.common.collect.Lists;
import net.minecraft.util.ResourceLocation;
import net.smileycorp.cosmeticwood.common.data.WoodHandler;

import java.util.List;
import java.util.function.Supplier;

public class WoodRegistryEntry {
    
    private final ResourceLocation block, item, defaultType;
    private final List<String> excludedModids;
    
    private WoodRegistryEntry(Builder builder) {
        this.block = builder.block;
        this.item = builder.item;
        this.defaultType = builder.defaultType;
        this.excludedModids = builder.excludedModids;
    }
    
    public ResourceLocation getBlock() {
        return block;
    }
    
    public ResourceLocation getItem() {
        return item;
    }
    
    public ResourceLocation getDefaultType() {
        return defaultType;
    }
    
    public List<String> getExcludedModids() {
        return excludedModids;
    }
    
    public static class Builder {
        
        private final ResourceLocation block;
        private ResourceLocation item;
        private ResourceLocation defaultType = WoodHandler.getDefault();
        private final List<String> excludedModids = Lists.newArrayList();
        
        public Builder(ResourceLocation block) {
            this.block = block;
            this.item = block;
        }
        
        public Builder item(ResourceLocation item) {
            this.item = item;
            return this;
        }
        
        public Builder defaultType(ResourceLocation type) {
            defaultType = type;
            return this;
        }
        
        public Builder exclude(String... modids) {
            for (String modid : modids) excludedModids.add(modid);
            return this;
        }
        
        public Builder conditionalExclude(Supplier<Boolean> condition, String... modids) {
            if (!condition.get()) exclude(modids);
            return this;
        }
        
        public WoodRegistryEntry build() {
            return new WoodRegistryEntry(this);
        }
        
    }
    
}
