package com.vitaldev.vitallibs.world;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class WorldBorderUtil {
    public JavaPlugin plugin;
    public WorldBorderUtil(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public static void setWorldBorder(World world, double size, double centerX, double centerZ) {
        WorldBorder border = world.getWorldBorder();
        border.setCenter(centerX, centerZ);
        border.setSize(size);
    }

    public static void setWorldBorderForPlayer(Player player, double size, double centerX, double centerZ) {
        setWorldBorder(player.getWorld(), size, centerX, centerZ);
    }

    public static boolean isPlayerInsideWorldBorder(Player player) {
        WorldBorder border = player.getWorld().getWorldBorder();
        return border.isInside(player.getLocation());
    }

    public static double getWorldBorderSize(World world) {
        return world.getWorldBorder().getSize();
    }

    public static double[] getWorldBorderCenter(World world) {
        WorldBorder border = world.getWorldBorder();
        return new double[] {border.getCenter().getX(), border.getCenter().getZ()};
    }

    public static void sendBorderMessageToPlayers(World world, String message) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getWorld().equals(world) && !isPlayerInsideWorldBorder(player)) {
                player.sendMessage(message);
            }
        }
    }

    public static void resetWorldBorder(World world) {
        WorldBorder border = world.getWorldBorder();
        border.setSize(30000000);
    }

    public void startWorldBorder(World world, double borderSize, int shrinkTimeInSeconds) {
        WorldBorder border = world.getWorldBorder();
        border.setSize(borderSize);
        border.setWarningTime(0);
        border.setWarningDistance(0);
        border.setDamageBuffer(0.1);

        Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
            border.setSize(1, shrinkTimeInSeconds);
            border.setWarningDistance(0);

            Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
                border.setSize(30000000);
            }, 20L * shrinkTimeInSeconds);
        }, 20);
    }

}
