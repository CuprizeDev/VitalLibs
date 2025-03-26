package com.vitaldev.vitallibs.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class ActionBarUtil {

    private final Plugin plugin;
    private static Class<?> chatComponentClass;
    private static Class<?> chatPacketClass;
    private static Method chatSerializerMethod;
    private static Constructor<?> packetConstructor;

    static {
        try {
            chatComponentClass = Class.forName("net.md_5.bungee.api.chat.TextComponent");
            chatPacketClass = Class.forName("org.bukkit.craftbukkit." + getVersion() + ".entity.CraftPlayer");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ActionBarUtil(Plugin plugin) {
        this.plugin = plugin;
    }

    public void sendActionBar(Player player, String message) {
        player.spigot().sendMessage(net.md_5.bungee.api.ChatMessageType.ACTION_BAR, new net.md_5.bungee.api.chat.TextComponent(ChatUtil.color(message)));
    }

    public void sendActionBarToAll(String message) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            sendActionBar(player, message);
        }
    }

    public void sendActionBarWithDuration(Player player, String message, int duration) {
        new BukkitRunnable() {
            int count = duration / 2;

            @Override
            public void run() {
                if (count <= 0 || !player.isOnline()) {
                    cancel();
                    return;
                }
                sendActionBar(player, message);
                count--;
            }
        }.runTaskTimerAsynchronously(plugin, 0, 2);
    }

    private static String getVersion() {
        return Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    }
}