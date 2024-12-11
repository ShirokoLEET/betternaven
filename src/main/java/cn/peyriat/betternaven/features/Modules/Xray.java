package cn.peyriat.betternaven.features.Modules;

import cn.peyriat.betternaven.features.Module;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;

public class Xray extends Module {
    public static ArrayList<Block> blocks = new ArrayList<>();

    public Xray() {
        super("Xray", KeyEvent.VK_X); //KEY X
    }
    public static ArrayList<Block> get(){
        if(blocks.size()==0){
            Arrays.stream(Blocks.class.getDeclaredFields())
                    .filter(f -> f.getType().equals(Block.class))
                    .filter(f -> f.getName().toLowerCase().contains("ore"))
                    .toList()
                    .forEach(f -> {
                        try {
                            blocks.add((Block) f.get(null));
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    });
        }
        return blocks;
    }


}
