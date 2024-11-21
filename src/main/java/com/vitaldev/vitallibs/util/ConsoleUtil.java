package com.vitaldev.vitallibs.util;

import static org.bukkit.Bukkit.getServer;

public class ConsoleUtil {
    public static void sendMessage(String message) {
        getServer().getConsoleSender().sendMessage(ChatUtil.color(message));
    }
}
