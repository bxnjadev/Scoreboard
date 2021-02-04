package com.comugamers.scoreboard.util;

import org.bukkit.ChatColor;

public class UtilColor {

    public static String colorize(String text){
        return ChatColor.translateAlternateColorCodes('&',text);
    }

}
