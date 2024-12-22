package cn.peyriat.betternaven.features.functions;

import cn.peyriat.betternaven.features.Module;
import cn.peyriat.betternaven.features.ModuleManager;
import cn.peyriat.betternaven.features.helper.GameHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.MovementInputUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static cn.peyriat.betternaven.features.helper.GameHelper.isValidBlock;
import static cn.peyriat.betternaven.features.helper.GameHelper.nullCheck;

public class Eagle extends Module {
    public Eagle(String name, int key) {
        super(name, key);
    }

    private static final boolean holdingBlocks = true;
    private static final boolean onGround = true;
    private static final float[] pitch = {45, 90};
    private static final boolean sneak = false;
    private static final boolean left = false;
    private static final boolean right = false;
    private static final boolean forwards = false;
    private static final boolean backwards = true;
    private static final double edgeDistance = 0.3;
    private static final Minecraft mc = Minecraft.getInstance();
     // Example value, replace with actual value

    @SubscribeEvent
    public static void handleMovementInput(MovementInputUpdateEvent event) {
        if (!nullCheck()) return;
        Player player = mc.player;
        GameHelper.sendMessage("Im really using event");
        boolean shouldBeActive = !player.getAbilities().flying && shouldSneak(event);
        boolean isCloseToEdge = checkIfCloseToEdge(player, edgeDistance);
        if (isCloseToEdge) {
            GameHelper.sendMessage("Close to edge");
            event.getInput().shiftKeyDown = true;
        } else {
            GameHelper.sendMessage("Not close to edge");
            event.getInput().shiftKeyDown = false;
        }
    }

    @SubscribeEvent
    public static boolean shouldSneak(MovementInputUpdateEvent event) {
        if (!ModuleManager.modulesClass.Eagle.isEnabled) {
            return false;
        }
        if (holdingBlocks && !isValidBlock(mc.player.getMainHandItem()) && !isValidBlock(mc.player.getOffhandItem())) {
            return false;
        }
        if (onGround && !mc.player.isOnGround()) {
            return false;
        }
        if (!contains(pitch, mc.player.getXRot())) {
            return false;
        }
        if (sneak && !event.getInput().shiftKeyDown) {
            return false;
        }
        if (left && !event.getInput().left) {
            return false;
        }
        if (right && !event.getInput().right) {
            return false;
        }
        if (forwards && !event.getInput().up) {
            return false;
        }
        if (backwards && !event.getInput().down) {
            return false;
        }
        return true;
    }


    public static boolean checkIfCloseToEdge(Player player, double edgeDistance) {
        double fractionalX = Math.abs(player.getX() - Math.floor(player.getX()));
        double fractionalZ = Math.abs(player.getZ() - Math.floor(player.getZ()));
        return (1 - fractionalX) < edgeDistance || (1 - fractionalZ) < edgeDistance;
    }

    private static boolean contains(float[] array, float value) {
        for (float v : array) {
            if (v == value) {
                return true;
            }
        }
        return false;
    }
}