package net.smileycorp.cosmeticwood.common;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ConfigHandler {
    
    private static String[] excludedVariants;
    
    public static void syncConfig(FMLPreInitializationEvent event) {
        Configuration config = new Configuration(event.getSuggestedConfigurationFile());
        try{
            config.load();
            excludedVariants = config.get("spawns", "spawnEntities", new String[] {"raids:pillager-1"}, "Wh ich wood variants should be excluded from ").getStringList();
        } catch(Exception e) {
        } finally {
            if (config.hasChanged()) config.save();
        }
    }
    
}
