package cn.peyriat.betternaven.features;

import cn.peyriat.betternaven.Betternaven;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.blaze3d.vertex.PoseStack;

import java.nio.file.Files;


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
        JsonObject json = JsonParser.parseString(Files.readString(Betternaven.file.toPath())).getAsJsonObject();
        if(enabled){
            enabled();
            json.add(this.name,null);
        }
        else{
            disabled();
            try {
                json.remove(this.name);
            }
            catch (Exception e){
                Betternaven.LOGGER.error("Failed to remove the module enabled in the config file: "+this.name);
            }
        }
        Files.writeString(Betternaven.file.toPath(),json.toString());
    }
}