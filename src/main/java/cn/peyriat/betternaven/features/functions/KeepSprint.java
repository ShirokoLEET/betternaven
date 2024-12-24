package cn.peyriat.betternaven.features.functions;

import cn.peyriat.betternaven.features.Module;
import cn.peyriat.betternaven.features.helper.GameHelper;
import net.minecraft.client.Minecraft;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_K;

public class KeepSprint extends Module {
    public KeepSprint() {
        super("KeepSprint", GLFW_KEY_K);
    }

    @Override
    public void update() {
        Minecraft mc = Minecraft.getInstance();
        if (!GameHelper.nullCheck()) return;
    }
}
