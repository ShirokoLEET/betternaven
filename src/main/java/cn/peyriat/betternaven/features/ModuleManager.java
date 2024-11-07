package cn.peyriat.betternaven.features;

import cn.peyriat.betternaven.features.Modules.Keepsprint;
import cn.peyriat.betternaven.features.Modules.Xray;
import cn.peyriat.betternaven.features.Modules.XYZ;
import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class ModuleManager {
    public static class modulesClass {
        public static Module Keepsprint = new Keepsprint("Keepsprint", KeyEvent.VK_K);
        public static Module Xray = new Xray();
        public static Module XYZ = new XYZ("XYZ", KeyEvent.VK_B);
    }
    public static ArrayList<Module> modules;

    public static void getModules() throws IllegalAccessException {
        modules = new ArrayList<>();
        for(Field field : modulesClass.class.getDeclaredFields()){
            modules.add((Module) field.get(null));
        }
    }
}