package cn.peyriat.betternaven.features.functions;

import cn.peyriat.betternaven.features.Module;
import cn.peyriat.betternaven.features.helper.GameHelper;
import net.minecraft.client.Minecraft;

public class Keepsprint extends Module {
    public Keepsprint(String name,int key) {
        super(name,key);
    }

    @Override
    public void update() {
        Minecraft mc = Minecraft.getInstance();
        if (!GameHelper.nullCheck()) return;
    }
}
