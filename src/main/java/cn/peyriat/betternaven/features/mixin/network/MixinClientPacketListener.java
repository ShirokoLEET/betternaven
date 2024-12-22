package cn.peyriat.betternaven.features.mixin.network;

import cn.peyriat.betternaven.features.events.PostVelocityEvent;
import cn.peyriat.betternaven.features.helper.GameHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public abstract class MixinClientPacketListener {

    @Inject(method = "handleSetEntityMotion", at = @At("RETURN"))
    public void onHandleSetEntityMotion(ClientboundSetEntityMotionPacket packet, CallbackInfo ci) {
        if (!GameHelper.nullCheck()) return;
        if (packet.getId() == Minecraft.getInstance().player.getId()) {
            MinecraftForge.EVENT_BUS.post(new PostVelocityEvent());
        }
    }
}