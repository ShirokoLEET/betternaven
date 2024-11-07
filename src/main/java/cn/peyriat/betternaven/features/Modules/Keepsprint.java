package cn.peyriat.betternaven.features.Modules;
import cn.peyriat.betternaven.features.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;


public class Keepsprint extends Module {
    public Keepsprint(String name, int key) {
        super(name, key);
    }
    @Override
    public void enabled() {

    }

    @Override
    public void disabled() {

    }
    @Override
    public void update() {
        Minecraft MC = Minecraft.getInstance();
        if (MC.player == null) {
           return;
        }
        MC.player.displayClientMessage(new TextComponent(String.format("%.2f, %.2f, %.2f", MC.player.getDeltaMovement().x, MC.player.getDeltaMovement().y, MC.player.getDeltaMovement().z)), true);



    }
}




