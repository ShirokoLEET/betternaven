package cn.peyriat.betternaven.features.mixins;

import cn.peyriat.betternaven.features.ModuleManager;
import cn.peyriat.betternaven.features.Modules.Xray;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(Block.class)
public abstract class MixinBlock {

    @Inject(method = "shouldRenderFace",at = @At("RETURN"), cancellable = true)
    private static void renderFace(BlockState p_152445_, BlockGetter p_152446_, BlockPos p_152447_, Direction p_152448_, BlockPos p_152449_, CallbackInfoReturnable<Boolean> cir){
        if(ModuleManager.modulesClass.Xray.enabled && Xray.get().contains(p_152445_.getBlock())){
            cir.setReturnValue(true);
        }
        else if(ModuleManager.modulesClass.Xray.enabled){
            cir.setReturnValue(false);
        }
    }
}
