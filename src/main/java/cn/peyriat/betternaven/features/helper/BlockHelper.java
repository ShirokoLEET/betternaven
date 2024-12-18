package cn.peyriat.betternaven.features.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.network.chat.TextComponent;

import java.util.ArrayList;
import java.util.List;

public class BlockHelper {
    private final int color;
    private final BlockPos blockPos;

    public BlockHelper(BlockPos blockPos, int color) {
        this.blockPos = blockPos;
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }

    public static List<BlockHelper> searchBlocks() {
        Minecraft mc = Minecraft.getInstance();
        List<BlockHelper> foundBlocks = new ArrayList<>();

        if (mc.level != null && mc.player != null) {
            // 减小搜索范围
            int offsetRange = 5;  // 将范围缩小到 5，避免过大的搜索区域
            for (BlockPos temppos : BlockPos.betweenClosed(
                    mc.player.blockPosition().offset(-offsetRange, -offsetRange, -offsetRange),
                    mc.player.blockPosition().offset(offsetRange, offsetRange, offsetRange))) {

                if (mc.level.getBlockState(temppos).getBlock() == Blocks.IRON_ORE) {
                    // 直接将 temppos 作为真实的世界坐标，不需要额外的偏移
                    foundBlocks.add(new BlockHelper(temppos, 0x00000));

                    // 发送消息时直接使用 temppos 坐标
                    mc.player.sendMessage(new TextComponent("Iron Ore Found at: " + temppos.getX() + ", " + temppos.getY() + ", " + temppos.getZ()), mc.player.getUUID());
                }
            }
        }
        return foundBlocks;
    }


}