package cn.peyriat.betternaven.features.functions.xray;
import cn.peyriat.betternaven.Betternaven;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import cn.peyriat.betternaven.features.helper.BlockData;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.common.Tags;
import org.apache.logging.log4j.Level;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class XrayJsonStore
{
    private static final String FILE = "block_store.json";
    private static final String CONFIG_DIR = Minecraft.getInstance().gameDirectory + "/config/";
    private static final Random rand = new Random();
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public boolean created = false;
    private File jsonFile;

    // This should only be initialised once
    public XrayJsonStore()
    {
        File configDir = new File(CONFIG_DIR, String.valueOf(Betternaven.MOD_ID));

        if( !configDir.exists() )
            configDir.mkdirs();

        jsonFile = new File(CONFIG_DIR + Betternaven.MOD_ID, FILE);
        if( !jsonFile.exists() ) {
            this.created = true;

            // Create a file with nothing inside
            this.write(new ArrayList<BlockData.SerializableBlockData>());
        }
    }

    public void write(ArrayList<BlockData> blockData) {
        List<BlockData.SerializableBlockData> simpleBlockData = new ArrayList<>();
        blockData.forEach( e -> simpleBlockData.add(new BlockData.SerializableBlockData(e.getEntryName(), e.getBlockName(), e.getColor(), e.isDrawing(), e.getOrder())) );

        this.write(simpleBlockData);
    }

    private static void write(List<BlockData.SerializableBlockData> simpleBlockData) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(CONFIG_DIR + Betternaven.MOD_ID, FILE)))) {
            gson.toJson(simpleBlockData, writer);
        }
        catch (IOException e) {
            Betternaven.LOGGER.log(Level.ERROR, "Failed to write json data to " + FILE);
        }
    }

    public List<BlockData.SerializableBlockData> read() {
        if( !jsonFile.exists() )
            return new ArrayList<>();

        try
        {
            Type type = new TypeToken<List<BlockData.SerializableBlockData>>() {}.getType();
            try (BufferedReader reader = new BufferedReader(new FileReader(jsonFile)))
            {
                return gson.fromJson(reader, type);
            }
            catch (JsonSyntaxException ex) {
                Betternaven.LOGGER.log(Level.ERROR, "Failed to read json data from " + FILE);
            }
        }
        catch (IOException e)
        {
            Betternaven.LOGGER.log(Level.ERROR, "Failed to read json data from " + FILE);
        }

        return new ArrayList<>();
    }

    public static List<BlockData.SerializableBlockData> populateDefault() {
        List<BlockData.SerializableBlockData> oresData = new ArrayList<>();
        Tags.Blocks.ORES.getValues().forEach(e -> {
            if( e.getRegistryName() == null )
                return;

            oresData.add(new BlockData.SerializableBlockData(new TranslatableComponent(e.getDescriptionId()).getString(),
                    e.getRegistryName().toString(),
                    (rand.nextInt(255) << 16) + (rand.nextInt(255) << 8) + rand.nextInt(255),
                    false,
                    0)
            );
        });

        for (int i = 0; i < oresData.size(); i++)
            oresData.get(i).setOrder(i);

        Betternaven.LOGGER.info("Setting up default syncRenderList");
        XrayJsonStore.write(oresData);
        return oresData;
    }
}