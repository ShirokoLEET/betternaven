package cn.peyriat.betternaven.features.helper;

import cn.peyriat.betternaven.features.Module;
import cn.peyriat.betternaven.features.ModuleManager;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.client.KeyMapping;


import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber
public class KeyboardHelper {
    public static final List<KeyMapping> KEY_BINDINGS = new ArrayList<>();

    static {
        try {
            for (Module module : ModuleManager.getModules()) {
                KEY_BINDINGS.add(new KeyMapping(module.name, KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, module.key, "Betternaven"));
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void init() {
        for (KeyMapping keyMapping : KEY_BINDINGS) {
            ClientRegistry.registerKeyBinding(keyMapping);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onKeyPressed(InputEvent.KeyInputEvent event) throws Exception {
        for (KeyMapping keyMapping : KEY_BINDINGS) {
            if (keyMapping.consumeClick()) {

                switch (keyMapping.getName()) {
                    case "Keepsprint":
                        ModuleManager.modulesClass.Keepsprint.toggle();
                        break;
                    case "HUD":
                        ModuleManager.modulesClass.HUD.toggle();
                        break;
                    case "GiftEsp":
                        ModuleManager.modulesClass.GiftEsp.toggle();
                        break;
                }
            }
        }
    }
}