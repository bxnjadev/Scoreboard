package com.comugamers.scoreboard.listeners;

import com.comugamers.scoreboard.handler.ScoreboardHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;

public class PlayerChangeGameModeListener implements Listener {

    private final ScoreboardHandler scoreboardHandler;
    public PlayerChangeGameModeListener(ScoreboardHandler scoreboardHandler){
        this.scoreboardHandler = scoreboardHandler;
    }

    @EventHandler
    public void onGameMode(PlayerGameModeChangeEvent event){

        Player player = event.getPlayer();
        scoreboardHandler.updateField(player,"%gamemode%");

    }

}
