package net.smileycorp.cosmeticwood.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

public class TileContext {

    private static ResourceLocation WOOD_TILE_TYPE = null;
    private static boolean GL_COLOUR_FROZEN = false;
    private static ResourceLocation OVERLAY, SUB_OVERLAY;
    private static boolean IS_OVERLAY;
    private static boolean BYPASS_FREEZE;

    public static void resetTileData() {
        WOOD_TILE_TYPE = null;
        OVERLAY = null;
        SUB_OVERLAY = null;
        IS_OVERLAY = false;
    }

    public static ResourceLocation getWoodTileType() {
        return WOOD_TILE_TYPE;
    }

    public static void setWoodTileType(ResourceLocation type) {
        WOOD_TILE_TYPE = type;
    }

    public static boolean isGlColourFrozen() {
        return GL_COLOUR_FROZEN &! BYPASS_FREEZE;
    }

    public static void freezeGLColour() {
        GL_COLOUR_FROZEN = true;
    }

    public static void unfreezeGLColour() {
        GL_COLOUR_FROZEN = false;
        GlStateManager.color(1, 1, 1, 1);
    }

    public static void setOverlayTexture(ResourceLocation loc) {
        OVERLAY = loc;
    }

    public static void setSubOverlayTexture(ResourceLocation loc) {
        SUB_OVERLAY = loc;
    }

    public static ResourceLocation getOverlayTexture() {
        IResourceManager rm = Minecraft.getMinecraft().getResourceManager();
        if (SUB_OVERLAY != null) try {
            rm.getResource(SUB_OVERLAY);
            return SUB_OVERLAY;
        } catch (Exception ignored) {}
        if (OVERLAY != null) try {
            rm.getResource(OVERLAY);
            return OVERLAY;
        } catch (Exception ignored) {}
        return null;
    }

    public static void markOverlay() {
        IS_OVERLAY = true;
    }

    public static boolean isOverlay() {
        return IS_OVERLAY;
    }

    public static void setBypassFreeze(boolean bypass) {
        BYPASS_FREEZE = bypass;
    }

}
