package com.comugamers.scoreboard.listeners;

import com.comugamers.scoreboard.handler.ScoreboardHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    private final ScoreboardHandler scoreboardHandler;

    public PlayerQuitListener(ScoreboardHandler scoreboardHandler) {
        this.scoreboardHandler = scoreboardHandler;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        scoreboardHandler.breakScoreboard(event.getPlayer());
    }

}
