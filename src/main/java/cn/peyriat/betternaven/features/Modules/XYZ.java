package cn.peyriat.betternaven.features.Modules;

import cn.peyriat.betternaven.features.Module;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.Vec3;

import java.awt.*;

public class XYZ extends Module {
    public XYZ(String name, int key) {
        super(name, key);
    }

    @Override
    public void render(PoseStack matrixStack) throws Exception {
        Minecraft MC = Minecraft.getInstance();
        if(!MC.options.keyShift.isDown() && MC.player!=null){
            Vec3 pos = MC.player.position();
            MC.font.drawShadow(matrixStack,"XYZ: "+Math.round(pos.x*1000.0D)/1000.0D+", "+Math.round(pos.y*1000.0D)/1000.0D+", "+Math.round(pos.z*1000.0D)/1000.0D,0,2, Color.WHITE.getRGB());
        }
    }
}
