package net.smileycorp.cosmeticwood.api;

import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;
import net.smileycorp.cosmeticwood.common.data.WoodHandler;

import java.util.List;
import java.util.function.Supplier;

public class WoodRegistryEntry {
    
    private final ResourceLocation block, item, defaultType;
    private final List<String> excludedModids;
    private String inventoryVariant;
    
    private WoodRegistryEntry(Builder builder) {
        block = builder.block;
        item = builder.item;
        defaultType = builder.defaultType;
        excludedModids = builder.excludedModids;
        inventoryVariant = builder.inventoryVariant;
    }
    
    public static WoodRegistryEntry fromJson(JsonElement element) {
        if (!(element instanceof JsonObject)) return null;
        JsonObject obj = (JsonObject) element;
        Builder builder = new Builder(new ResourceLocation(obj.get("block").getAsString()));
        if (obj.has("item")) builder.item(new ResourceLocation(obj.get("item").getAsString()));
        if (obj.has("default_type")) builder.defaultType(new ResourceLocation(obj.get("default_type").getAsString()));
        if (obj.has("exclude_modids")) obj.getAsJsonArray("exclude_modids").forEach(e -> builder.exclude(e.getAsString()));
        if (obj.has("inventory_variant")) builder.inventoryVariant = obj.get("inventory_variant").getAsString();
        return builder.build();
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
    
    public String getInventoryVariant() {
        return inventoryVariant;
    }
    
    public static class Builder {
        
        private final ResourceLocation block;
        private ResourceLocation item;
        private ResourceLocation defaultType = WoodHandler.getDefault();
        private final List<String> excludedModids = Lists.newArrayList();
        private String inventoryVariant = "inventory";
        
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
        
        public Builder inventory(String inventoryVariant) {
            this.inventoryVariant = inventoryVariant;
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
