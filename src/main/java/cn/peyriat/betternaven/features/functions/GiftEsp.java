package cn.peyriat.betternaven.features.functions;
import cn.peyriat.betternaven.features.Module;
import cn.peyriat.betternaven.features.ModuleManager;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.client.event.RenderLevelLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class GiftEsp extends Module {
    public GiftEsp(String name,int key) {
        super(name,key);
    }
    @SubscribeEvent
    public static void onRenderWorldLast(RenderLevelLastEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null) return;

        PoseStack matrixStack = event.getPoseStack();
        for (BlockPos pos : BlockPos.betweenClosed(mc.player.blockPosition().offset(-50, -50, -50), mc.player.blockPosition().offset(50, 50, 50))) {
            if (mc.level.getBlockState(pos).getBlock() == Blocks.IRON_ORE) {
                LevelRenderer.renderLineBox(matrixStack, null, pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1, 1.0F, 0.0F, 0.0F, 1.0F);
            }
        }
    }


}
