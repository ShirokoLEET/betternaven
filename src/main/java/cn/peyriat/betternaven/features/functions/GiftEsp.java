package cn.peyriat.betternaven.features.functions;

import cn.peyriat.betternaven.features.Module;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.RenderLevelLastEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import cn.peyriat.betternaven.features.helper.BlockHelper.BlockProps;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Mod.EventBusSubscriber
public class GiftEsp extends Module {
    public GiftEsp(String name, int key) {
        super(name, key);
    }

    private static VertexBuffer vertexBuffer;
    private static List<BlockProps> blockPropsList = new ArrayList<>();
    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();
    static Minecraft mc = Minecraft.getInstance();



    @SubscribeEvent
    public static void onChunkLoad(ChunkEvent.Load event) {
        executorService.submit(GiftEsp::findBlocks);
    }

    @SubscribeEvent
    public static void onPlayerRespawn(ClientPlayerNetworkEvent.RespawnEvent event) {
        blockPropsList.clear();
        executorService.submit(GiftEsp::findBlocks);
    }

    @SubscribeEvent
    public static void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        executorService.submit(GiftEsp::findBlocks);
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        executorService.submit(GiftEsp::findBlocks);
    }

    @SubscribeEvent
    public static void renderblocks(RenderLevelLastEvent event) {
        if (vertexBuffer == null) {
            vertexBuffer = new VertexBuffer();
            Tesselator tessellator = Tesselator.getInstance();
            BufferBuilder buffer = tessellator.getBuilder();
            buffer.begin(VertexFormat.Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR);
            var opacity = 0F;
            blockPropsList.forEach(blockProps -> {
                if (blockProps == null) {
                    return;
                }
                final float size = 1.0f;
                final int x = blockProps.getPos().getX(), y = blockProps.getPos().getY(), z = blockProps.getPos().getZ();
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
            });
            buffer.end();
            vertexBuffer.upload(buffer);

            vertexBuffer.bind();
            vertexBuffer.draw();
            VertexBuffer.unbind();
        }
        if (vertexBuffer != null) {
            Vec3 playerPos = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
            RenderSystem.depthMask(false);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            PoseStack poseStack = event.getPoseStack();
            poseStack.pushPose();
            RenderSystem.setShader(GameRenderer::getPositionColorShader);
            RenderSystem.applyModelViewMatrix();
            RenderSystem.depthFunc(GL11.GL_ALWAYS);
            poseStack.mulPoseMatrix(poseStack.last().pose());
            poseStack.translate(-playerPos.x(), -playerPos.y(), -playerPos.z());
            vertexBuffer.bind();
            vertexBuffer.drawWithShader(poseStack.last().pose(), event.getProjectionMatrix(), Objects.requireNonNull(RenderSystem.getShader()));
            VertexBuffer.unbind();
            RenderSystem.depthFunc(GL11.GL_LEQUAL);
            poseStack.popPose();
            RenderSystem.applyModelViewMatrix();
        }
    }

    private static synchronized void findBlocks() {
        if (mc.level != null && mc.player != null) {
            blockPropsList.clear();
            for (BlockPos pos : BlockPos.betweenClosed(mc.player.blockPosition().offset(-50, -50, -50), mc.player.blockPosition().offset(50, 50, 50))) {
                if (mc.level.getBlockState(pos).getBlock() == Blocks.IRON_ORE) {
                    blockPropsList.add(new BlockProps(pos));
                    mc.player.sendMessage(new TextComponent("Iron Ore Found at: " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ()), mc.player.getUUID());
                }
            }
        }
    }
}