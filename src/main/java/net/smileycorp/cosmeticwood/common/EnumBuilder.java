package net.smileycorp.cosmeticwood.common;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.BlockWorkbench;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.oredict.OreDictionary;

public class EnumBuilder {
	

	public static final PropertyEnum<WoodType> PropertyWoodType = PropertyEnum.<WoodType>create("type", WoodType.class);

	public static void buildProperties() {
		for (String ore:OreDictionary.getOreNames()) {
			if (ore.contains("log")) {
				for (ItemStack stack : OreDictionary.getOres(ore)) {
					if (stack.getItem() instanceof ItemBlock) {
						String name = ModDefinitions.format(stack.getItem().getUnlocalizedName());
						EnumHelper.addEnum(WoodType.class, name, new Class[] {String.class}, name);
					}
				}
			}
		}
	}
	
	public static WoodType getDefault() {
		return getType("Oak");
	}
	
	public static WoodType getType(String key) {
		if (ENUM_MAP.containsKey(key)) {
			return ENUM_MAP.get(key);
		} else {
			for (WoodType type : WoodType.values()) {
				if (key.equals(type.getName())) {
					ENUM_MAP.put(key, type);
					return type;
				}
			}
		}
		return getType("Oak");
	}
	
	static Map<String, WoodType> ENUM_MAP = new HashMap<String, WoodType>();
	
	public enum WoodType implements IStringSerializable {
		Oak("Oak");
		
		final String name;
		
		WoodType(String name) {
			this.name=name;
		}

		@Override
		public String getName() {
			return this.name;
		}
		
		@Override
		public String toString() {
			return this.name;
		}
		
	}

}
