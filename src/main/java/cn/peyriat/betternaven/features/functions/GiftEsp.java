package cn.peyriat.betternaven.features.functions;

import cn.peyriat.betternaven.features.Module;
import cn.peyriat.betternaven.features.ModuleManager;
import cn.peyriat.betternaven.features.helper.BlockHelper;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.RenderLevelLastEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.opengl.GL11;

import java.util.List;
import java.util.Objects;

@Mod.EventBusSubscriber
public class GiftEsp extends Module {
    public GiftEsp(String name, int key) {
        super(name, key);
    }

    private static VertexBuffer vertexBuffer;
    private static List<BlockHelper> renderList;
    static Minecraft mc = Minecraft.getInstance();
    private static boolean needupdate = false;

    @SubscribeEvent
    public static void onPlayerLoggedIn(ClientPlayerNetworkEvent.LoggedInEvent event) {
        updateRenderList();
    }

    @SubscribeEvent
    public static void onChunkLoad(ChunkEvent.Load event) {
        updateRenderList();
    }

    @SubscribeEvent
    public static void onPlayerRespawn(ClientPlayerNetworkEvent.RespawnEvent event) {
        renderList.clear();
        updateRenderList();
    }

    @SubscribeEvent
    public static void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        updateRenderList();
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        updateRenderList();
    }

    private static void updateRenderList() {
        renderList = BlockHelper.searchBlocks();
        needupdate = true;
    }

    @SubscribeEvent
    public static void renderblocks(RenderLevelLastEvent event) {
        if (mc.level == null || mc.player == null) return;
        if (!ModuleManager.modulesClass.GiftEsp.isEnabled) return;

        if (vertexBuffer == null || needupdate) {
            needupdate = false;
            vertexBuffer = new VertexBuffer();
            Tesselator tessellator = Tesselator.getInstance();
            BufferBuilder buffer = tessellator.getBuilder();
            buffer.begin(VertexFormat.Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR);
            var opacity = 1F;
            final float size = 1.0f;
            for (BlockHelper block : renderList) {
                final float x = block.getBlockPos().getX();
                final float y = block.getBlockPos().getY();
                final float z = block.getBlockPos().getZ();
                final float red = 1.0f;
                final float green = 0.0f;
                final float blue = 0.0f;
                buffer.vertex(x, y + size, z).color(red, green, blue, opacity).endVertex();
                buffer.vertex(x + size, y + size, z).color(red, green, blue, opacity).endVertex();
                buffer.vertex(x + size, y + size, z).color(red, green, blue, opacity).endVertex();
                buffer.vertex(x + size, y + size, z + size).color(red, green, blue, opacity).endVertex();
                buffer.vertex(x + size, y + size, z + size).color(red, green, blue, opacity).endVertex();
                buffer.vertex(x, y + size, z + size).color(red, green, blue, opacity).endVertex();
                buffer.vertex(x, y + size, z + size).color(red, green, blue, opacity).endVertex();
                buffer.vertex(x, y + size, z).color(red, green, blue, opacity).endVertex();

                // BOTTOM
                buffer.vertex(x + size, y, z).color(red, green, blue, opacity).endVertex();
                buffer.vertex(x + size, y, z + size).color(red, green, blue, opacity).endVertex();
                buffer.vertex(x + size, y, z + size).color(red, green, blue, opacity).endVertex();
                buffer.vertex(x, y, z + size).color(red, green, blue, opacity).endVertex();
                buffer.vertex(x, y, z + size).color(red, green, blue, opacity).endVertex();
                buffer.vertex(x, y, z).color(red, green, blue, opacity).endVertex();
                buffer.vertex(x, y, z).color(red, green, blue, opacity).endVertex();
                buffer.vertex(x + size, y, z).color(red, green, blue, opacity).endVertex();

                // Edge 1
                buffer.vertex(x + size, y, z + size).color(red, green, blue, opacity).endVertex();
                buffer.vertex(x + size, y + size, z + size).color(red, green, blue, opacity).endVertex();

                // Edge 2
                buffer.vertex(x + size, y, z).color(red, green, blue, opacity).endVertex();
                buffer.vertex(x + size, y + size, z).color(red, green, blue, opacity).endVertex();

                // Edge 3
                buffer.vertex(x, y, z + size).color(red, green, blue, opacity).endVertex();
                buffer.vertex(x, y + size, z + size).color(red, green, blue, opacity).endVertex();

                // Edge 4
                buffer.vertex(x, y, z).color(red, green, blue, opacity).endVertex();
                buffer.vertex(x, y + size, z).color(red, green, blue, opacity).endVertex();
            }
            buffer.end();
            vertexBuffer.upload(buffer);
        }
        if (vertexBuffer != null) {
            Vec3 view = Minecraft.getInstance().getEntityRenderDispatcher().camera.getPosition();

            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glEnable(GL11.GL_LINE_SMOOTH);
            GL11.glDisable(GL11.GL_DEPTH_TEST);

            PoseStack matrix = event.getPoseStack();
            matrix.pushPose();
            matrix.translate(-view.x, -view.y, -view.z);
            vertexBuffer.drawWithShader(matrix.last().pose(), event.getProjectionMatrix().copy(), Objects.requireNonNull(GameRenderer.getPositionColorShader()));
            matrix.popPose();
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_LINE_SMOOTH);
        }
    }
}
