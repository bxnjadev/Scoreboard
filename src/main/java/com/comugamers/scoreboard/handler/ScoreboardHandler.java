package com.comugamers.scoreboard.handler;

import com.comugamers.scoreboard.ScoreboardUser;
import com.comugamers.scoreboard.data.ScoreboardData;
import com.comugamers.scoreboard.replacer.ScoreboardReplacer;
import org.bukkit.entity.Player;

import java.util.Optional;

public interface ScoreboardHandler {

    void setScoreboard(Player player,String title, ScoreboardData scoreboardData, ScoreboardReplacer scoreboardReplacer);

    void breakScoreboard(Player player);

    Optional<ScoreboardUser> getScoreboard(Player player);

    void updateField(Player player, String field);

    void updateTitle(Player player, String title);

}
