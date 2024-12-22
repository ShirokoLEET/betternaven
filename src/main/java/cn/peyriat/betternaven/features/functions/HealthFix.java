package cn.peyriat.betternaven.features.functions;
import cn.peyriat.betternaven.features.Module;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.scores.Score;
import net.minecraft.world.scores.Scoreboard;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mod.EventBusSubscriber
public class HealthFix extends Module {
    public HealthFix(String name, int key) {
        super(name, key);
    }

    @SubscribeEvent
    public static void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntity() instanceof Player player) {
            Scoreboard scoreboard = player.getScoreboard();
            Score score = scoreboard.getOrCreatePlayerScore(player.getScoreboardName(), scoreboard.getObjective("belowname"));
            String scoreString = score.getScore() + "❤"; // Example score string
            float realHealth = extractHealth(scoreString);
            if (realHealth > 0) {
                player.setHealth(realHealth);
            }
        }
    }

    private static float extractHealth(String scoreString) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(scoreString);
        if (matcher.find()) {
            return Float.parseFloat(matcher.group());
        }
        return 0;
    }
}