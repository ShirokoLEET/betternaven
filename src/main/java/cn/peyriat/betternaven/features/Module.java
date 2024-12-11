package cn.peyriat.betternaven.features;

import cn.peyriat.betternaven.features.helper.ConfigHelper;
import com.mojang.blaze3d.vertex.PoseStack;


public class Module {

    public final String name;
    public final int key;
    public boolean enabled = false;

    public Module(String name, int key){
        this.name = name;
        this.key = key;
    }

    public void disabled() throws Exception{
    }

    public void update() throws Exception{
    }

    public void render(PoseStack matrixStack) throws Exception{
    }

    public void keyInput(int key) throws Exception{

    }

    public void enabled() throws Exception{
    }

    public void set(boolean enabled) throws Exception {
        this.enabled = enabled;
        if(enabled){
            enabled();
            ConfigHelper.setConfig(this.name,true);
        }
        else{
            disabled();
            ConfigHelper.setConfig(this.name,false);
            }
        }
    }
