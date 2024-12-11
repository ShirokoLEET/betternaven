package cn.peyriat.betternaven.features.helper;

import cn.peyriat.betternaven.Betternaven;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraftforge.client.settings.IKeyConflictContext;

public class KeyBindHelper {

    public static void init() {
        if (Boolean.parseBoolean(System.getProperty("java.awt.headless"))) {
        System.setProperty("java.awt.headless", "false");
            Betternaven.LOGGER.info("Keymapping already init.");
        }
    }



}
