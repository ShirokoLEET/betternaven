package cn.peyriat.betternaven.features.helper;

import cn.peyriat.betternaven.BetterNaven;
import cn.peyriat.betternaven.features.Module;
import cn.peyriat.betternaven.features.ModuleManager;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.nio.file.Files;


public class ConfigHelper {
    public static void init() {
        try {
            if (!BetterNaven.file.exists()) {
                BetterNaven.LOGGER.info("Prepare to create the Config.");
                if (BetterNaven.file.createNewFile())
                    BetterNaven.LOGGER.info("Config created.");
                else
                    BetterNaven.LOGGER.error("Failed to create the Config file.");
                ConfigHelper.saveDefaultConfig();
                BetterNaven.LOGGER.info("Config initialized.");
            } else {
                final JsonObject json = JsonParser.parseString(Files.readString(BetterNaven.file.toPath())).isJsonObject() ? JsonParser.parseString(Files.readString(BetterNaven.file.toPath())).getAsJsonObject() : new JsonObject();
                json.keySet().forEach(name -> {
                    ModuleManager.getModules().stream().filter(module -> module.getName().equals(name)).forEach(module -> module.set(json.get(name).getAsBoolean()));
                });
            }
        } catch (Exception e) {
            BetterNaven.LOGGER.error("Failed on Config initialization.");
        }
    }

    private static void saveDefaultConfig() {
        try {
            final JsonObject json = new JsonObject();
            ModuleManager.getModules().forEach(module -> json.addProperty(module.getName(), false));
            Files.writeString(BetterNaven.file.toPath(), json.toString());
        } catch (final Exception exception) {
            BetterNaven.LOGGER.warn(exception);
        }
    }

    public static void setConfig(final String module, final boolean state) {
        try {
            final JsonObject json = JsonParser.parseString(Files.readString(BetterNaven.file.toPath())).getAsJsonObject();
            json.addProperty(module, state);
            Files.writeString(BetterNaven.file.toPath(), json.toString());
        } catch (final Exception exception) {
            BetterNaven.LOGGER.warn(exception);
        }
    }
}
