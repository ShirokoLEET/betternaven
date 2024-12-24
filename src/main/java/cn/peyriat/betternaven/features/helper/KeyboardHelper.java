package cn.peyriat.betternaven.features.helper;

import cn.peyriat.betternaven.features.ModuleManager;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber
public class KeyboardHelper {
    public static final List<KeyMapping> KEY_BINDINGS = ModuleManager.getModules().stream().map(module -> new KeyMapping(module.getName(), KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, module.getKey(), "Betternaven")).collect(Collectors.toList());

    public static void init() {
        KEY_BINDINGS.forEach(ClientRegistry::registerKeyBinding);
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onKeyPressed(InputEvent.KeyInputEvent event) {
        KEY_BINDINGS.stream().filter(KeyMapping::consumeClick).forEach(keyMapping -> ModuleManager.getModule(keyMapping.getName()).toggle());
    }
}