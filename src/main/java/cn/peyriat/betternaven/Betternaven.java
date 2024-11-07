package cn.peyriat.betternaven;
import cn.peyriat.betternaven.features.ModuleManager;
import cn.peyriat.betternaven.features.Module;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
import java.io.IOException;
import java.nio.file.Files;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("betternaven")
@Mod.EventBusSubscriber
public class Betternaven {
    public static Logger LOGGER = LogManager.getLogger();
    public static File file = new File("config/better_naven.json");
    public Betternaven() throws IllegalAccessException, IOException {
        MinecraftForge.EVENT_BUS.register(this);
        ModuleManager.getModules();
        init();

    }
    public void init() throws IOException{
        if(!file.exists()){
            Files.write(file.toPath(),"{}".getBytes());
        }
        JsonObject json = JsonParser.parseString(Files.readString(file.toPath())).getAsJsonObject();
        if(ModuleManager.modules!=null){
            for(String name:json.keySet()){
                ModuleManager.modules.stream().filter(module -> module.name.equals(name)).forEach(module -> {
                    module.enabled = true;
                });
            }
        }

        if(Boolean.parseBoolean(System.getProperty("java.awt.headless"))){
            System.setProperty("java.awt.headless","false");
        }

        LOGGER.info("ExampleMod is done.");
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







