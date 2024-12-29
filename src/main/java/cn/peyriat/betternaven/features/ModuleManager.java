package cn.peyriat.betternaven.features;
import cn.peyriat.betternaven.features.functions.HUD;
import cn.peyriat.betternaven.features.functions.KeepSprint;
import cn.peyriat.betternaven.features.functions.Eagle;
import cn.peyriat.betternaven.features.functions.Velocity;
import cn.peyriat.betternaven.features.functions.HealthFix;
import com.google.common.collect.Lists;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    private static List<Module> modules;

    public static void registerModules() {
        modules = Lists.newArrayList(new KeepSprint(),
                new HUD(),
                new Eagle(),
                new Velocity(),
                new HealthFix()
        );
    }

    @SuppressWarnings("unchecked")
    public static <T extends Module> T getModule(final Class<T> klass) {
        return (T) modules.stream().filter(module -> module.getClass() == klass).findFirst().get();
    }

    public static Module getModule(final String name) {
        return modules.stream().filter(module -> name.equals(module.getName())).findFirst().get();
    }

    public static List<Module> getModules() {
        return modules;
    }


}
