package cn.peyriat.betternaven.features.functions;
import cn.peyriat.betternaven.Betternaven;
import cn.peyriat.betternaven.features.Module;
import cn.peyriat.betternaven.features.events.PostVelocityEvent;
import cn.peyriat.betternaven.features.helper.GameHelper;
import net.minecraft.client.Minecraft;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.glfw.GLFW;

import java.util.concurrent.TimeUnit;

public class Velocity extends Module {
    public Velocity(String name, int key) {
        super(name, key);
    }
        private final int minDelay = 0;
        private final int maxDelay = 2;
        private final int chance = 100;
        //private final boolean targetNearbyCheck = false;
        private final boolean ignoreLiquid = true;
        private static Minecraft mc = Minecraft.getInstance();

        @SubscribeEvent
        public void onVelocity(PostVelocityEvent event) {
            if (!GameHelper.nullCheck()) return;
            if (chance == 0) return;
            if (mc.player.hurtTime <= 0) return;
            if (ignoreLiquid && GameHelper.inLiquid()) return;
            //if (targetNearbyCheck && !Utils.isTargetNearby())
            // return;

            if (chance != 100) {
                double ch = Math.random();
                if (ch >= chance / 100)
                    return;
            }

            long delay = (long) (Math.random() * (maxDelay - minDelay) + minDelay);
            if (maxDelay == 0 || delay == 0) {
                if (mc.player.isOnGround()) {
                    mc.player.jumpFromGround();
                }
            } else {
                Betternaven.getExecutor().schedule(() -> {
                    if (mc.player.isOnGround()) {
                        mc.player.jumpFromGround();
                    };
                }, delay, TimeUnit.MILLISECONDS);
            }
            // Simulate releasing and pressing the W key
            if (GLFW.glfwGetKey(mc.getWindow().getWindow(), GLFW.GLFW_KEY_W) == GLFW.GLFW_PRESS) {
               mc.options.keyUp.setDown(false);
               mc.options.keyUp.setDown(true);
            }else {mc.options.keyUp.setDown(false);}
        }
}


