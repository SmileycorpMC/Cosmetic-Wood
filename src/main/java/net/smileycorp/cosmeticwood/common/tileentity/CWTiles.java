package net.smileycorp.cosmeticwood.common.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.smileycorp.cosmeticwood.common.ModDefinitions;

public class CWTiles {

	public static void registerContent() {
		registerTileEntity(TileEntitySimpleWood.class, "SimpleWood");
		//registerTileEntity(TileCWTconHopper.class, "WoodenHopper");
	}
	
	public static void registerTileEntity (Class <? extends TileEntity> te, String name) {
		GameRegistry.registerTileEntity(te, ModDefinitions.getResource(name));
	}

}
