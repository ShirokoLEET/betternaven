package cn.peyriat.betternaven.features.functions;

import cn.peyriat.betternaven.features.Module;
import cn.peyriat.betternaven.features.ModuleManager;
import com.mojang.blaze3d.vertex.PoseStack;
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
        if (!MC.options.keyShift.isDown() && MC.player != null) {
            Vec3 pos = MC.player.position();

            MC.font.draw(matrixStack, "XYZ: " + Math.round(pos.x) + ", " + Math.round(pos.y) + ", " + Math.round(pos.z), 0, 2, Color.WHITE.getRGB());
            MC.font.draw(matrixStack, "Keepsprint: " + (ModuleManager.modulesClass.Keepsprint.isEnabled ? "ON" : "OFF"), 0, 12, ModuleManager.modulesClass.Keepsprint.isEnabled ? Color.GREEN.getRGB() : Color.RED.getRGB());
            MC.font.drawShadow(matrixStack, "GiftEsp: " + (ModuleManager.modulesClass.GiftEsp.isEnabled ? "ON" : "OFF"), 0, 22, ModuleManager.modulesClass.GiftEsp.isEnabled ? Color.GREEN.getRGB() : Color.RED.getRGB());
        }
    }
}
