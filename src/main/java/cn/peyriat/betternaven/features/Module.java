package cn.peyriat.betternaven.features;

import cn.peyriat.betternaven.features.helper.ConfigHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraftforge.common.MinecraftForge;

public class Module {
    private final String name;
    private int key;
    private boolean isEnabled = false;

    public Module(final String name, final int key) {
        this.name = name;
        this.key = key;
    }

    public int getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public void onEnable() {}

    public void onDisable() {}

    public boolean isEnabled() {
        return isEnabled;
    }

    public void update() {}

    public void render(PoseStack matrixStack) {}

    public void keyInput(int key) {}

    public void set(boolean enabled) {
        this.isEnabled = enabled;
        if (enabled) {
            ConfigHelper.setConfig(this.name, true);
            onEnable();
            MinecraftForge.EVENT_BUS.register(this);
        } else {
            ConfigHelper.setConfig(this.name, false);
            onDisable();
            MinecraftForge.EVENT_BUS.unregister(this);
        }
    }

    public void toggle() {
        this.set(!this.isEnabled);
    }
}