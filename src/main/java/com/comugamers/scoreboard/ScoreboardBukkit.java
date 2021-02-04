package com.comugamers.scoreboard;

import com.comugamers.scoreboard.handler.ScoreboardHandler;
import com.comugamers.scoreboard.handler.SimpleScoreboardHandler;
import com.comugamers.scoreboard.listeners.PlayerChangeGameModeListener;
import com.comugamers.scoreboard.listeners.PlayerChangedWorldListener;
import com.comugamers.scoreboard.listeners.PlayerJoinListener;
import com.comugamers.scoreboard.listeners.PlayerQuitListener;
import com.comugamers.scoreboard.replacer.ScoreboardReplacer;
import com.comugamers.scoreboard.util.Configuration;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class ScoreboardBukkit extends JavaPlugin {

    @Override
    public void onEnable() {

        Configuration configuration = new Configuration(this, "scoreboard");
        ScoreboardHandler scoreboardHandler = new SimpleScoreboardHandler();
        ScoreboardReplacer scoreboardReplacer = new BukkitScoreboardReplacer();

        register(new PlayerJoinListener(scoreboardReplacer, configuration, scoreboardHandler));
        register(new PlayerQuitListener(scoreboardHandler));
        register(new PlayerChangeGameModeListener(scoreboardHandler));
        register(new PlayerChangedWorldListener(scoreboardHandler));

        getLogger().info("Scoreboard activada");

    }

    @Override
    public void onDisable() {
        getLogger().info("Chau");
    }

    private void register(Listener listener){
        Bukkit.getPluginManager().registerEvents(listener,this);
    }

}
