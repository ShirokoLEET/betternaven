package cn.peyriat.betternaven;

import cn.peyriat.betternaven.features.Module;
import cn.peyriat.betternaven.features.ModuleManager;
import cn.peyriat.betternaven.features.helper.ConfigHelper;
import cn.peyriat.betternaven.features.helper.KeyboardHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("betternaven")
@Mod.EventBusSubscriber

public class BetterNaven {
    public static final String MOD_ID = "BetterNaven";

    public static Logger LOGGER = LogManager.getLogger();
    public static File file = new File("better_naven.json");
    private static final ScheduledExecutorService ex = Executors.newScheduledThreadPool(4);

    public BetterNaven() {
        MinecraftForge.EVENT_BUS.register(this);
        ModuleManager.registerModules();
        ConfigHelper.init();
        KeyboardHelper.init();
    }

    @SubscribeEvent
    public void playerTick(TickEvent.PlayerTickEvent event) {
        ModuleManager.getModules().stream().filter(Module::isEnabled).forEach(Module::update);
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void renderTick(RenderGameOverlayEvent.Pre event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
            ModuleManager.getModules().stream().filter(Module::isEnabled).forEach(module -> module.render(event.getMatrixStack()));
        }
    }

    public static ScheduledExecutorService getExecutor() {
        return ex;
    }

}







