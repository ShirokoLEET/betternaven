package cn.peyriat.betternaven.features;
import cn.peyriat.betternaven.features.functions.HUD;
import cn.peyriat.betternaven.features.functions.Keepsprint;
import cn.peyriat.betternaven.features.functions.Eagle;
import cn.peyriat.betternaven.features.functions.Velocity;
import cn.peyriat.betternaven.features.functions.HealthFix;
import org.lwjgl.glfw.GLFW;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    public static class modulesClass {
        public static Module Keepsprint = new Keepsprint("Keepsprint",GLFW.GLFW_KEY_K);
        public static Module HUD = new HUD("HUD",GLFW.GLFW_KEY_P);
        public static Module Eagle = new Eagle("Eagle",GLFW.GLFW_KEY_F);
        public static Module Velocity = new Velocity("Velocity",GLFW.GLFW_KEY_V);
        public static Module HealthFix = new HealthFix("HealthFix",GLFW.GLFW_KEY_H);
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
