package cn.peyriat.betternaven.features.functions.xray;


import cn.peyriat.betternaven.Betternaven;
import cn.peyriat.betternaven.features.functions.Xray;
import net.minecraftforge.client.event.RenderLevelLastEvent;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;



@Mod.EventBusSubscriber(modid = Betternaven.MOD_ID, value = Dist.CLIENT)
public class XrayEvents
{
    @SubscribeEvent
    public static void pickupItem( BlockEvent.BreakEvent event ) {
        XrayRenderEnqueue.checkBlock( event.getPos(), event.getState(), false);
    }

    @SubscribeEvent
    public static void placeItem( BlockEvent.EntityPlaceEvent event ) {
        XrayRenderEnqueue.checkBlock( event.getPos(), event.getState(), true);
    }

    @SubscribeEvent
    public static void tickEnd( TickEvent.ClientTickEvent event ) {
        if ( event.phase == TickEvent.Phase.END && Minecraft.getInstance().player != null && Minecraft.getInstance().level != null ) {
            XrayController.requestBlockFinder( false );
        }
    }

    @SubscribeEvent
    public static void onWorldRenderLast( RenderLevelLastEvent event ) // Called when drawing the world.
    {
        if ( XrayController.isXRayActive() && Minecraft.getInstance().player != null )
        {
            // this is a world pos of the player
            XrayRender.renderBlocks(event);
        }
    }
}
