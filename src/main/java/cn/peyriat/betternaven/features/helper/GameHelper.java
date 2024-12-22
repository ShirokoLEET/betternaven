package cn.peyriat.betternaven.features.helper;

import cn.peyriat.betternaven.Betternaven;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.logging.log4j.Logger;
import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.HashSet;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

public class GameHelper {
    private static final Random rand = new Random();
    public static final Minecraft mc = Minecraft.getInstance();
    public static final TextComponent PREFIX = new TextComponent("&7[&dBN&7]&r ");
    public static HashSet<String> friends = new HashSet<>();
    public static HashSet<String> enemies = new HashSet<>();
    public static final Logger log = Betternaven.LOGGER;

    public static boolean randomizeBoolean() {
        return Math.random() >= 0.5;
    }

    public static int randomizeInt(double min, double max) {
        return (int) Math.round(randomizeDouble(min, max));
    }

    public static double randomizeDouble(double min, double max) {
        return Math.random() * (max - min) + min;
    }

    public static boolean inFov(float fov, @NotNull BlockPos blockPos) {
        return inFov(fov, blockPos.getX(), blockPos.getZ());
    }

    public static boolean inFov(float fov, @NotNull Entity entity) {
        return inFov(fov, entity.getX(), entity.getZ());
    }

    public static boolean inFov(float fov, @NotNull Entity self, @NotNull Entity target) {
        return inFov(self.getYRot(), fov, target.getX(), target.getZ());
    }

    public static boolean inFov(float fov, final double n2, final double n3) {
        fov *= 0.5F;
        final double fovToPoint = getFov(n2, n3);
        if (fovToPoint > 0.0) {
            return fovToPoint < fov;
        } else return fovToPoint > -fov;
    }

    public static boolean inFov(float yaw, float fov, final double n2, final double n3) {
        fov *= 0.5F;
        final double fovToPoint = getFov(yaw, n2, n3);
        if (fovToPoint > 0.0) {
            return fovToPoint < fov;
        } else return fovToPoint > -fov;
    }

    public static @Range(from = -180, to = 180) double getFov(final double posX, final double posZ) {
        return getFov(mc.player.getYRot(), posX, posZ);
    }

    public static @Range(from = -180, to = 180) double getFov(final float yaw, final double posX, final double posZ) {
        return Mth.wrapDegrees((yaw - angle(posX, posZ)) % 360.0f);
    }

    private static final Queue<String> delayedMessage = new ConcurrentLinkedQueue<>();

    public static void sendMessage(String txt) {
        if (nullCheck()) {
            String m = formatColor(PREFIX.getString() + replace(txt));
            mc.player.sendMessage(new TextComponent(m), mc.player.getUUID());
        }
    }

    public static String formatColor(String txt) {
        return txt.replaceAll("&", "§");
    }
    public static @Nullable String replace(String string) {
        return string;
    }

    public static boolean nullCheck() {
        return mc.player != null && mc.level != null;
    }

    public static float angle(final double n, final double n2) {
        return (float) (Math.atan2(n - mc.player.getX(), n2 - mc.player.getZ()) * 57.295780181884766 * -1.0);
    }

    public static boolean inLiquid() {
        return mc.player.isInWater() || mc.player.isInLava();
    }

    public static boolean isValidBlock(ItemStack stack) {
        if (stack == null) {
            return false;
        }

        Block block = Block.byItem(stack.getItem());
        if (block == null) {
            return false;
        }

        if (block instanceof FallingBlock) {
            return false;
        }
        return !isBlockDisallowed(block);
    }
    public static final HashSet<Block> DISALLOWED_BLOCKS_TO_PLACE = new HashSet<Block>() {{
        add(Blocks.TNT);
        add(Blocks.COBWEB);
        add(Blocks.NETHER_PORTAL);
    }};

    public static final HashSet<Block> UNFAVORABLE_BLOCKS_TO_PLACE = new HashSet<Block>() {{
        add(Blocks.CRAFTING_TABLE);
        add(Blocks.JIGSAW);
        add(Blocks.SMITHING_TABLE);
        add(Blocks.FLETCHING_TABLE);
        add(Blocks.ENCHANTING_TABLE);
        add(Blocks.CAULDRON);
        add(Blocks.MAGMA_BLOCK);
    }};

    // Example method to demonstrate usage
    public static boolean isBlockUnfavourable(Block block) {
        return UNFAVORABLE_BLOCKS_TO_PLACE.contains(block);
    }
    public static boolean isBlockDisallowed(Block block) {
        return DISALLOWED_BLOCKS_TO_PLACE.contains(block);
    }

}
