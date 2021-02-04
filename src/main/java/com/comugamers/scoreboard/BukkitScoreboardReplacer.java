package com.comugamers.scoreboard;

import com.comugamers.scoreboard.replacer.ScoreboardReplacer;
import com.comugamers.scoreboard.util.UtilColor;
import org.bukkit.entity.Player;

public class BukkitScoreboardReplacer implements ScoreboardReplacer {

    @Override
    public String replace(Player player, String string) {
        return UtilColor.colorize(string.replace("%gamemode%",player.getGameMode().toString())
        .replace("%world_name%",player.getWorld().getName()));
    }
}
