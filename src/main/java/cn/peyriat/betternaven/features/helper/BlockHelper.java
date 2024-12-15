package cn.peyriat.betternaven.features.helper;
import net.minecraft.core.BlockPos;

public class BlockHelper {
    public static class BlockProps {
        private final BlockPos blockPos;
        private static int color = 0;

        public BlockProps(BlockPos blockPos) {
            this.blockPos = blockPos;
        }

        public BlockPos getPos() {
            return blockPos;
        }

        public static int getColor() {
            return color;
        }
    }
}
