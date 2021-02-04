package com.comugamers.scoreboard.listeners;

import com.comugamers.scoreboard.handler.ScoreboardHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class PlayerChangedWorldListener implements Listener {

    private final ScoreboardHandler scoreboardHandler;
    public PlayerChangedWorldListener(ScoreboardHandler scoreboardHandler){
        this.scoreboardHandler = scoreboardHandler;
    }

    @EventHandler
    public void onChangeWorld(PlayerChangedWorldEvent event){
        scoreboardHandler.updateField(event.getPlayer(),"%world_name%");
    }

}
