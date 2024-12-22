package cn.peyriat.betternaven.features.functions;

import cn.peyriat.betternaven.features.Module;
import cn.peyriat.betternaven.features.ModuleManager;
import cn.peyriat.betternaven.features.helper.GameHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Game;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.Vec3;

import java.awt.*;

public class HUD extends Module {
    public HUD(String name, int key) {
        super(name, key);
    }

    @Override
    public void render(PoseStack matrixStack) throws Exception {
        Minecraft MC = Minecraft.getInstance();
        Vec3 pos = MC.player.position();
        if (!GameHelper.nullCheck()) return;
            MC.font.draw(matrixStack, "XYZ: " + Math.round(pos.x) + ", " + Math.round(pos.y) + ", " + Math.round(pos.z), 0, 2, Color.WHITE.getRGB());
            MC.font.draw(matrixStack, "Eagle: " + (ModuleManager.modulesClass.Eagle.isEnabled ? "ON" : "OFF"), 0, 12, ModuleManager.modulesClass.Eagle.isEnabled ? Color.GREEN.getRGB() : Color.RED.getRGB());
            MC.font.draw(matrixStack, "HealthFix: " + (ModuleManager.modulesClass.HealthFix.isEnabled ? "ON" : "OFF"), 0, 22, ModuleManager.modulesClass.HealthFix.isEnabled ? Color.GREEN.getRGB() : Color.RED.getRGB());
            MC.font.draw(matrixStack, "Velocity: " + (ModuleManager.modulesClass.Velocity.isEnabled ? "ON" : "OFF"), 0, 32, ModuleManager.modulesClass.Velocity.isEnabled ? Color.GREEN.getRGB() : Color.RED.getRGB());
            MC.font.draw(matrixStack, "Player Rot:" + MC.player.getYRot() + ", " + MC.player.getXRot(), 0, 42, Color.WHITE.getRGB());
            MC.font.draw(matrixStack,Eagle.checkIfCloseToEdge(MC.player, 0.3) ? "Close to edge" : "Not close to edge", 0, 52, Eagle.checkIfCloseToEdge(MC.player,0.3) ? Color.RED.getRGB() : Color.GREEN.getRGB());
    }
}
