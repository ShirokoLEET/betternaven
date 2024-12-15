package cn.peyriat.betternaven.features.helper;
import cn.peyriat.betternaven.Betternaven;
import cn.peyriat.betternaven.features.Module;
import cn.peyriat.betternaven.features.ModuleManager;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


import java.nio.file.Files;


public class ConfigHelper {
    public static void init() {
        try {
            if (!Betternaven.file.exists()) {
                Betternaven.LOGGER.info("Prepare to create the Config.");
               if (Betternaven.file.createNewFile()) Betternaven.LOGGER.info("Config created."); else Betternaven.LOGGER.error("Failed to create the Config file.");
                ConfigHelper.saveDefaultConfig();
                JsonObject json = JsonParser.parseString(Files.readString(Betternaven.file.toPath())).isJsonObject() ? JsonParser.parseString(Files.readString(Betternaven.file.toPath())).getAsJsonObject() : new JsonObject();
                if (ModuleManager.modules != null) {
                    for (String name : json.keySet()) {
                        ModuleManager.modules.stream().filter(module -> module.name.equals(name)).forEach(module -> module.isEnabled = json.get(name).getAsBoolean());
                    }
                }
                Betternaven.LOGGER.info("Config initialized.");
            }
        }catch (Exception e){
                Betternaven.LOGGER.error("Failed on Config initialization.");
            }
        }
    private static void saveDefaultConfig() throws Exception {
        JsonObject json = new JsonObject();
        if (ModuleManager.modules != null) {
            for (Module module : ModuleManager.modules) {
                json.addProperty(module.name, false);
        }
    }
        Files.writeString(Betternaven.file.toPath(), json.toString());
    }

    public static void setConfig(String module, Boolean o) throws Exception {
        JsonObject json = JsonParser.parseString(Files.readString(Betternaven.file.toPath())).getAsJsonObject();
        json.addProperty(module, o);
        Files.writeString(Betternaven.file.toPath(), json.toString());
    }
}
