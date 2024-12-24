package cn.peyriat.betternaven.features.functions;

import cn.peyriat.betternaven.features.Module;
import cn.peyriat.betternaven.features.ModuleManager;
import cn.peyriat.betternaven.features.helper.GameHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.AirBlock;
import net.minecraftforge.client.event.MovementInputUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static cn.peyriat.betternaven.features.helper.GameHelper.isValidBlock;
import static cn.peyriat.betternaven.features.helper.GameHelper.nullCheck;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F;

public class Eagle extends Module {
    public Eagle() {
        super("Eagle", GLFW_KEY_F);
    }

    private static final boolean holdingBlocks = true;
    private static final boolean onGround = true;
    private static final int[] pitch = {45, 90};
    private static final boolean sneak = false;
    private static final boolean left = false;
    private static final boolean right = false;
    private static final boolean forwards = false;
    private static final boolean backwards = true;
    private static final double edgeDistance = 0.3;
    private static final Minecraft mc = Minecraft.getInstance();
    // Example value, replace with actual value

    @SubscribeEvent
    public void handleMovementInput(MovementInputUpdateEvent event) {
        if (!nullCheck()) return;
//        final boolean isCloseToEdge = checkIfCloseToEdge(mc.player, 0.3); 不好使

        Minecraft.getInstance().options.keyShift.setDown(isAirBlockBelow() && shouldSneak(event));
    }

    public static boolean isAirBlockBelow() {
        for (double x = -edgeDistance; x < edgeDistance; x += 0.1) {
            for (double z = -edgeDistance; z < edgeDistance; z += 0.1) { //高版本下蹲有延迟，所以需要提前蹲下
                if (mc.level.getBlockState(new BlockPos(mc.player.position().add(x, -1, z))).getBlock() instanceof AirBlock) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onDisable() {
        Minecraft.getInstance().options.keyShift.setDown(false);
    }

    @SubscribeEvent
    public boolean shouldSneak(MovementInputUpdateEvent event) {
        if (!ModuleManager.getModule(Eagle.class).isEnabled()) {
            return false;
        }
        if (holdingBlocks && !isValidBlock(mc.player.getMainHandItem()) && !isValidBlock(mc.player.getOffhandItem())) {

            return false;
        }
        if (onGround && !mc.player.isOnGround()) {

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

}