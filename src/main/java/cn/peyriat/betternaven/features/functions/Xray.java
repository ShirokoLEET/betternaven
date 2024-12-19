package cn.peyriat.betternaven.features.functions;

import cn.peyriat.betternaven.Betternaven;
import cn.peyriat.betternaven.features.Module; // Ensure this is the correct Module class
import cn.peyriat.betternaven.features.functions.xray.*;
import cn.peyriat.betternaven.features.helper.ConfigHelper;
import cn.peyriat.betternaven.features.helper.BlockData;
import net.minecraft.client.resources.language.I18n;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.ArrayList;
import java.util.List;
@Mod.EventBusSubscriber(modid = Betternaven.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Xray extends Module {
    public Xray(String name, int key) {
        super(name, key);
    }

    // This contains all of the game's blocks to allow us to reference them
    // when needed. This allows us to avoid continually rebuilding
    public static XrayGameBlockStore gameBlockStore = new XrayGameBlockStore();
    public static XrayJsonStore blockStore = new XrayJsonStore();

    public static void setup() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, XrayConfig.SPEC);
    }

    @SubscribeEvent
    public static void onSetup(final FMLCommonSetupEvent event) {
        Betternaven.LOGGER.debug(I18n.get("xray.debug.init"));

        List<BlockData.SerializableBlockData> data = blockStore.read();
        if (data.isEmpty())
            return;

        ArrayList<BlockData> map = XrayBlockStore.getFromSimpleBlockList(data);
        XrayController.getBlockStore().setStore(map);
    }

    @SubscribeEvent
    public static void onLoadComplete(FMLLoadCompleteEvent event) {
        Xray.gameBlockStore.populate();
    }
}
