package net.smileycorp.cosmeticwood.common;

import com.google.common.collect.Maps;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.Map;

public class ConfigHandler {
    
    
    private static Map<ResourceLocation, ResourceLocation> duplicatedTypes;
    private static String[] duplicatedTypesStr = {
            "forestry:oak_(fireproof)-minecraft:oak", "forestry:spruce_(fireproof)-minecraft:spruce", "forestry:birch_(fireproof)-minecraft:birch",
            "forestry:jungle_(fireproof)-minecraft:jungle", "forestry:acacia_(fireproof)-minecraft:acacia", "forestry:dark_oak_(fireproof)-minecraft:dark_oak",
            "forestry:larch_(fireproof)-forestry:larch", "forestry:teak_(fireproof)-forestry:teak", "forestry:desert_acacia_(fireproof)-forestry:desert_acacia",
            "forestry:lime_(fireproof)-forestry:lime", "forestry:chestnut_(fireproof)-forestry:chestnut", "forestry:wenge_(fireproof)-forestry:wenge",
            "forestry:baobab_(fireproof)-forestry:baobab", "forestry:sequoia_(fireproof)-forestry:sequoia", "forestry:kapok_(fireproof)-forestry:kapok",
            "forestry:ebony_(fireproof)-forestry:ebony", "forestry:mahogany_(fireproof)-forestry:mahogany", "forestry:balsa_(fireproof)-forestry:balsa",
            "forestry:willow_(fireproof)-forestry:willow", "forestry:walnut_(fireproof)-forestry:walnut", "forestry:greenheart_(fireproof)-forestry:greenheart",
            "forestry:cherry_(fireproof)-forestry:cherry", "forestry:mahoe_(fireproof)-forestry:mahoe", "forestry:poplar_(fireproof)-forestry:poplar",
            "forestry:palm_(fireproof)-forestry:palm", "forestry:papaya_(fireproof)-forestry:papaya", "forestry:pine_(fireproof)-forestry:pine",
            "forestry:plum_(fireproof)-forestry:plum", "forestry:maple_(fireproof)-forestry:maple", "forestry:citrus_(fireproof)-forestry:citrus",
            "forestry:giant_sequoia_(fireproof)-forestry:giant_sequoia", "forestry:ipe_(fireproof)-forestry:ipe", "forestry:padauk_(fireproof)-forestry:padauk",
            "forestry:cocobolo_(fireproof)-forestry:cocobolo", "forestry:zebrawood_(fireproof)-forestry:zebrawood",
            "quark:vertical_oak-minecraft:oak", "quark:vertical_spruce-minecraft:spruce", "quark:vertical_birch-minecraft:birch",
            "quark:vertical_jungle-minecraft:jungle", "quark:vertical_acacia-minecraft:acacia", "quark:vertical_dark_oak-minecraft:dark_oak",
            "quark:vertical_white_stained-quark:white_stained", "quark:vertical_orange_stained-quark:orange_stained",
            "quark:vertical_magenta_stained-quark:magenta_stained", "quark:vertical_light_blue_stained-quark:light_blue_stained",
            "quark:vertical_yellow_stained-quark:yellow_stained", "quark:vertical_lime_stained-quark:lime_stained",
            "quark:vertical_pink_stained-quark:pink_stained", "quark:vertical_gray_stained-quark:gray_stained",
            "quark:vertical_light_gray_stained-quark:light_gray_stained", "quark:vertical_cyan_stained-quark:cyan_stained",
            "quark:vertical_purple_stained-quark:purple_stained", "quark:vertical_blue_stained-quark:blue_stained",
            "quark:vertical_brown_stained-quark:brown_stained", "quark:vertical_green_stained-quark:green_stained",
            "quark:vertical_red_stained-quark:red_stained", "quark:vertical_black_stained-quark:black_stained"
    };
    
    public static boolean displayVariantsInJei;
    
    public static void syncConfig(FMLPreInitializationEvent event) {
        Configuration config = new Configuration(event.getSuggestedConfigurationFile());
        try{
            config.load();
            duplicatedTypesStr = config.get("general", "duplicatedTypes", duplicatedTypesStr, "Which wood variants should be treated as identical? (format should be name1-name2, e.g. quark:vertical_acacia-minecraft:acacia will make quark vertical acacia count as acacia for crafting)").getStringList();
            displayVariantsInJei = config.get("client", "displayVariantsInJei", false, "Whether to display all wood variants in JEI search?").getBoolean();
        } catch(Exception e) {} finally {
            if (config.hasChanged()) config.save();
        }
    }
    
    public static ResourceLocation getOtherName(ResourceLocation registry) {
        if (duplicatedTypes == null) {
            duplicatedTypes = Maps.newHashMap();
            for (String str : duplicatedTypesStr) {
                try {
                    String[] names = str.split("-");
                    ResourceLocation loc1 = new ResourceLocation(names[0]);
                    ResourceLocation loc2 = new ResourceLocation(names[1]);
                    duplicatedTypes.put(loc1, loc2);
                } catch (Exception e) {
                    CosmeticWood.logError("Error parsing duplicated wood type " + str, e);
                }
            }
        }
        return duplicatedTypes.containsKey(registry) ? duplicatedTypes.get(registry) : registry;
    }
    
}
