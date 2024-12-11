package cn.peyriat.betternaven;
import cn.peyriat.betternaven.features.ModuleManager;
import cn.peyriat.betternaven.features.Module;
import cn.peyriat.betternaven.features.helper.ConfigHelper;
import cn.peyriat.betternaven.features.helper.KeyBindHelper;
import com.sun.jna.platform.KeyboardUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.TickEvent;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.io.File;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("betternaven")
@Mod.EventBusSubscriber

public class Betternaven {
    public static Logger LOGGER = LogManager.getLogger();
    public static File file = new File("better_naven.json");
    public Betternaven() throws Exception {

        MinecraftForge.EVENT_BUS.register(this);
        ModuleManager.getModules();
        ConfigHelper.init();
        KeyBindHelper.init();

    }




    @SubscribeEvent
    public void PlayerTick(TickEvent.PlayerTickEvent event) throws Exception {
        if(ModuleManager.modules!=null){
            for(Module module: ModuleManager.modules.stream().filter(module -> module.enabled).toList()){
                module.update();
            }
        }
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void RenderTick(RenderGameOverlayEvent.Pre event) throws Exception {
        if(event.getType()==RenderGameOverlayEvent.ElementType.ALL && ModuleManager.modules!=null){
            for(Module module: ModuleManager.modules.stream().filter(module -> module.enabled).toList()){
                module.render(event.getMatrixStack());
            }
        }
    }

    @SubscribeEvent
    public void keyInputEvent(InputEvent.KeyInputEvent event) throws Exception {
        if(ModuleManager.modules!=null){
            for(Module module : ModuleManager.modules){
                if(module.key!=-1){

                    int key = event.getKey();

                    if(module.enabled){
                        module.keyInput(key);
                    }

                    if(KeyboardUtils.isPressed(module.key)){
                        module.set(!module.enabled);
                        if (Minecraft.getInstance().player != null) {
                            Minecraft.getInstance().player.sendMessage(Component.nullToEmpty(module.name+": "+module.enabled),Minecraft.getInstance().player.getUUID());
                        }
                    }
                }
            }
        }
    }






}







