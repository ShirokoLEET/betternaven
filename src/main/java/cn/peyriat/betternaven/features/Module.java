package cn.peyriat.betternaven.features;

import cn.peyriat.betternaven.features.helper.ConfigHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraftforge.common.MinecraftForge;

public class Module {

    public final String name;
    public int key;
    public boolean isEnabled = false;

    public Module(String name, int key) {
        this.name = name;
        this.key = key;
    }

    public void disabled() throws Exception {
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    public void update() throws Exception {
    }

    public void render(PoseStack matrixStack) throws Exception {
    }

    public void keyInput(int key) throws Exception {
    }

    public void enabled() throws Exception {
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void set(boolean enabled) throws Exception {
        this.isEnabled = enabled;
        if (enabled) {
            ConfigHelper.setConfig(this.name, true);
            enabled();

        } else {
            ConfigHelper.setConfig(this.name, false);
            disabled();

        }
    }

    public void toggle() throws Exception {
        this.set(!this.isEnabled);
    }
}