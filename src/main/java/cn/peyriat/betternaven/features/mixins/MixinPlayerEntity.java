package cn.peyriat.betternaven.features.mixins;
import cn.peyriat.betternaven.features.ModuleManager;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import net.minecraft.world.phys.Vec3;

@Mixin(Player.class)

public class MixinPlayerEntity {
    @Redirect(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/Vec3;multiply(DDD)Lnet/minecraft/world/phys/Vec3;"))
    private Vec3 hookSlowVelocity(Vec3 instance, double x, double y, double z) {
        if ((Object) this == Minecraft.getInstance().player && ModuleManager.modulesClass.Keepsprint.enabled) {
            x = z = 1;
        }
        return instance.multiply(x, y, z);
    }
}