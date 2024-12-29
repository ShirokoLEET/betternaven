package cn.peyriat.betternaven.features.functions;

import cn.peyriat.betternaven.features.Module;
import cn.peyriat.betternaven.features.ModuleManager;
import cn.peyriat.betternaven.features.helper.GameHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Game;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.phys.Vec3;

import java.awt.*;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_P;

public class HUD extends Module {
    public HUD() {
        super("HUD", GLFW_KEY_P);
    }

    @Override
    public void render(PoseStack matrixStack) {
        if (!GameHelper.nullCheck()) return;
        final Minecraft mc = Minecraft.getInstance();
        final Vec3 pos = mc.player.position();
        mc.font.draw(matrixStack, String.format("XYZ: %d, %d, %d", Math.round(pos.x), Math.round(pos.y), Math.round(pos.z)), 0, 2, Color.WHITE.getRGB());
        mc.font.draw(matrixStack, "Eagle: " + getStateString(Eagle.class), 0, 12, getStateColor(Eagle.class));
        mc.font.draw(matrixStack, "HealthFix: " + getStateString(HealthFix.class), 0, 22, getStateColor(HealthFix.class));
        mc.font.draw(matrixStack, "Velocity: " + getStateString(Velocity.class), 0, 32, getStateColor(Velocity.class));
        mc.font.draw(matrixStack, "Player Rot:" + mc.player.getYRot() % 360 + ", " + mc.player.getXRot(), 0, 42, Color.WHITE.getRGB());
        final boolean isAirBlock = Eagle.isAirBlockBelow();// mc.level.getBlockState(mc.player.blockPosition().below()).getBlock() instanceof AirBlock;
        mc.font.draw(matrixStack, isAirBlock ? "Close to edge" : "Not close to edge", 0, 52, isAirBlock ? Color.RED.getRGB() : Color.GREEN.getRGB());
    }


}
