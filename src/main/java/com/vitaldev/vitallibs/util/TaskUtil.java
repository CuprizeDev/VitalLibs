package com.vitaldev.vitallibs.util;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;

public class TaskUtil {
    private final Plugin plugin;
    private static HashMap<Player, HashMap<String, BukkitTask>> playerTasks = new HashMap<>();

    public TaskUtil(Plugin plugin) {
        this.plugin = plugin;
    }

    public void scheduleTask(Player player, String taskType, Runnable code, int seconds) {
        cancelTask(player, taskType);

        BukkitTask newTask = new BukkitRunnable() {
            @Override
            public void run() {
                code.run();
                removeTask(player, taskType);
            }
        }.runTaskLater(plugin, seconds * 20L);

        playerTasks.computeIfAbsent(player, p -> new HashMap<>()).put(taskType, newTask);
    }

    public void cancelTask(Player player, String taskType) {
        if (playerTasks.containsKey(player)) {
            HashMap<String, BukkitTask> tasks = playerTasks.get(player);
            BukkitTask task = tasks.get(taskType);

            if (task != null && !task.isCancelled()) {
                task.cancel();
            }

            removeTask(player, taskType);
        }
    }

    public boolean isTaskRunning(Player player, String taskType) {
        if (!playerTasks.containsKey(player)) {
            return false;
        }

        BukkitTask task = playerTasks.get(player).get(taskType);
        return task != null && !task.isCancelled();
    }

    public void scheduleRepeatingTask(Player player, String taskType, Runnable code, int intervalSeconds) {
        cancelTask(player, taskType);
        BukkitTask newTask = new BukkitRunnable() {
            @Override
            public void run() {
                code.run();
            }
        }.runTaskTimer(plugin, 0L, intervalSeconds * 20L);
        playerTasks.computeIfAbsent(player, p -> new HashMap<>()).put(taskType, newTask);
    }

    private void removeTask(Player player, String taskType) {
        if (playerTasks.containsKey(player)) {
            HashMap<String, BukkitTask> tasks = playerTasks.get(player);
            tasks.remove(taskType);

            if (tasks.isEmpty()) {
                playerTasks.remove(player);
            }
        }
    }
}
