package com.vitaldev.vitallibs.util;

import org.bukkit.Bukkit;

import static org.bukkit.Bukkit.getServer;

public class ConsoleUtil {
    public static void sendMessage(String message) {
        getServer().getConsoleSender().sendMessage(ChatUtil.color(message));
    }

    public static void sendCommand(String command) {
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
    }

}
