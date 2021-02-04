package com.comugamers.scoreboard.listeners;

import com.comugamers.scoreboard.data.ScoreboardData;
import com.comugamers.scoreboard.handler.ScoreboardHandler;
import com.comugamers.scoreboard.replacer.ScoreboardReplacer;
import com.comugamers.scoreboard.util.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final ScoreboardReplacer scoreboardReplacer;
    private final Configuration configuration;
    private final ScoreboardHandler scoreboardHandler;
    public PlayerJoinListener(ScoreboardReplacer scoreboardReplacer, Configuration configuration, ScoreboardHandler scoreboardHandler){
        this.scoreboardReplacer = scoreboardReplacer;
        this.configuration = configuration;
        this.scoreboardHandler = scoreboardHandler;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){

        Player player = event.getPlayer();

        scoreboardHandler.setScoreboard(player,configuration.getString("Join.Title"),
                ScoreboardData.create(configuration.getStringList("Join.Scoreboard")),scoreboardReplacer);

    }

}
