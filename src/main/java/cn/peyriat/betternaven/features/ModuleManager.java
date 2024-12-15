package cn.peyriat.betternaven.features;

import cn.peyriat.betternaven.features.functions.GiftEsp;
import cn.peyriat.betternaven.features.functions.HUD;
import cn.peyriat.betternaven.features.functions.Keepsprint;
import org.lwjgl.glfw.GLFW;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    public static class modulesClass {
        public static Module Keepsprint = new Keepsprint("Keepsprint",GLFW.GLFW_KEY_K);
        public static Module GiftEsp = new GiftEsp("GiftEsp",GLFW.GLFW_KEY_X);
        public static Module HUD = new HUD("HUD",GLFW.GLFW_KEY_P);
    }
    public static ArrayList<Module> modules = new ArrayList<>();

    public static List<Module> getModules() throws IllegalAccessException {
        if (modules.isEmpty()) {
            for (Field field : modulesClass.class.getDeclaredFields()) {
                modules.add((Module) field.get(null));
            }
        }
        return modules;
    }
}
