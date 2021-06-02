package net.smileycorp.cosmeticwood.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModdedWoodHandler {
	
	private static boolean builtMap = false;
	private static Map<String, String[]> modWoods = new HashMap<String, String[]>();
	
	private static void buildMap() {
		modWoods.put("minecraft", new String[]{"oak", "spruce", "birch", "jungle", "acacia", "dark_oak"});
		modWoods.put("rustic", new String[]{"olive", "ironwood"});
	}
	
	public void registerModWoods(String modid, String[] types) {
		modWoods.put("rustic", types);
	}

	public static List<String> getTypes(String modid) {
		if (!builtMap) buildMap();
		List<String> result = WoodHandler.getTypes();
		for (String name : modWoods.get("minecraft")) {
			result.remove(name);
		}
		if (modWoods.containsKey(modid)) {
			for (String name : modWoods.get(modid)) {
				result.remove(name);
			}
		}
		return result;
	}
}
