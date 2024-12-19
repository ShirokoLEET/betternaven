package cn.peyriat.betternaven.features.functions.xray;

import cn.peyriat.betternaven.features.Module;
import cn.peyriat.betternaven.features.ModuleManager;
import cn.peyriat.betternaven.features.functions.xray.XrayConfig;
import cn.peyriat.betternaven.features.functions.xray.XrayRender;
import cn.peyriat.betternaven.features.functions.xray.XrayRenderEnqueue;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import cn.peyriat.betternaven.features.functions.xray.XrayBlockStore;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.Util;
import net.minecraft.network.chat.TranslatableComponent;
import cn.peyriat.betternaven.features.helper.RenderBlockProps;

import java.util.*;

public class XrayController extends Module {
    public XrayController(String name,int key) {
        super(name,key);
    }
    private static final int maxStepsToScan = 5;
    private static boolean isSearching = false;

    // Block blackList
    // Todo: move this to a configurable thing


    public static ArrayList<Block> blackList = new ArrayList<>() {{
        add(Blocks.AIR);
        add(Blocks.BEDROCK);
        add(Blocks.STONE);
        add(Blocks.GRASS);
        add(Blocks.DIRT);
    }};

    private static ChunkPos lastChunkPos = null;

    public static final Set<RenderBlockProps> syncRenderList = Collections.synchronizedSet(new HashSet<>()); // this is accessed by threads

    /**
     * Global blockStore used for:
     * [Rendering, GUI, Configuration Handling]
     */
    private static XrayBlockStore blockStore = new XrayBlockStore();

    // Thread management

    // Draw states
    private static boolean xrayActive = false; // Off by default
    private static boolean lavaActive = XrayConfig.store.lavaActive.get();

    public static XrayBlockStore getBlockStore() {
        return blockStore;
    }

    // Public accessors
    public static boolean isXRayActive() {
        return xrayActive && Minecraft.getInstance().level != null && Minecraft.getInstance().player != null;
    }

    public static void toggleXRay() {
        if (!xrayActive) // enable drawing
        {
            syncRenderList.clear(); // first, clear the buffer
            xrayActive = true; // then, enable drawing
            requestBlockFinder(true); // finally, force a refresh

            if (ModuleManager.modulesClass.XrayController.isEnabled && Minecraft.getInstance().player != null)
                Minecraft.getInstance().player.displayClientMessage(new TranslatableComponent("xray.toggle.activated"), false);
        } else // disable drawing
        {
            if (ModuleManager.modulesClass.XrayController.isEnabled && Minecraft.getInstance().player != null)
                Minecraft.getInstance().player.displayClientMessage(new TranslatableComponent("xray.toggle.deactivated"), false);

            xrayActive = false;
        }
    }

    public static boolean isLavaActive() {
        return lavaActive;
    }

    public static void toggleLava() {
        lavaActive = !lavaActive;
        XrayConfig.store.lavaActive.set(lavaActive);
    }

    public static int getRadius() {
        return Mth.clamp(XrayConfig.store.radius.get(), 0, maxStepsToScan) * 3;
    }

    public static int getHalfRange() {
        return Math.max(0, getRadius() / 2);
    }

    public static int getVisualRadius() {
        return Math.max(1, getRadius());
    }

    public static void incrementCurrentDist() {
        if (XrayConfig.store.radius.get() < maxStepsToScan)
            XrayConfig.store.radius.set(XrayConfig.store.radius.get() + 1);
        else
            XrayConfig.store.radius.set(0);
    }

    public static void decrementCurrentDist() {
        if (XrayConfig.store.radius.get() > 0)
            XrayConfig.store.radius.set(XrayConfig.store.radius.get() - 1);
        else
            XrayConfig.store.radius.set(maxStepsToScan);
    }

    /**
     * Precondition: world and player must be not null
     * Has player moved since the last region scan?
     * This method does not update the last player location so consecutive
     * calls yield the same result.
     *
     * @return true if the player has moved since the last blockFinder call
     */
    private static boolean playerHasMoved() {
        if (Minecraft.getInstance().player == null)
            return false;

        ChunkPos plyChunkPos = Minecraft.getInstance().player.chunkPosition();
        int range = getHalfRange();

        return lastChunkPos == null ||
                plyChunkPos.x > lastChunkPos.x + range || plyChunkPos.x < lastChunkPos.x - range ||
                plyChunkPos.z > lastChunkPos.z + range || plyChunkPos.z < lastChunkPos.z - range;
    }

    private static void updatePlayerPosition() {
        lastChunkPos = Minecraft.getInstance().player.chunkPosition();
    }

    /**
     * Starts a region scan thread if possible, that is if:
     * - we actually want to draw syncRenderList
     * - we are not already scanning an area
     * - either the player has moved since the last call
     * - or we want to (and can) force a scan
     *
     * @param force should we force a block scan even if the player hasn't moved?
     */
    public static synchronized void requestBlockFinder(boolean force) {
        if (isXRayActive() && (force || playerHasMoved()) && !isSearching) // world/player check done by xrayActive()
        {
            updatePlayerPosition(); // since we're about to run, update the last known position
            Util.backgroundExecutor().execute(() -> {
                isSearching = true;
                // Scan for the blocks
                Set<RenderBlockProps> c = XrayRenderEnqueue.blockFinder();
                syncRenderList.clear();
                syncRenderList.addAll(c);
                isSearching = false;

                // Tell the render to update
                XrayRender.requestedRefresh = true;
            });
        }
    }
}
