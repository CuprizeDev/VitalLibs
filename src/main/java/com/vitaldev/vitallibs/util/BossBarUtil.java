package com.vitaldev.vitallibs.util;

import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BossBarUtil {

    private static final Map<UUID, BossBar> activeBossBars = new HashMap<>();
    private final TaskUtil taskUtil;

    public BossBarUtil(TaskUtil taskUtil) {
        this.taskUtil = taskUtil;
    }

    public void createBossBar(Player player, String title, BarColor color, BarStyle style, double progress) {
        BossBar bossBar = org.bukkit.Bukkit.createBossBar(title, color, style);
        bossBar.setProgress(Math.max(0.0, Math.min(1.0, progress)));
        bossBar.addPlayer(player);
        activeBossBars.put(player.getUniqueId(), bossBar);
    }

    public void removeBossBar(Player player) {
        BossBar bossBar = activeBossBars.remove(player.getUniqueId());
        if (bossBar != null) {
            bossBar.removeAll();
        }
    }

    public void updateTitle(Player player, String newTitle) {
        BossBar bossBar = activeBossBars.get(player.getUniqueId());
        if (bossBar != null) {
            bossBar.setTitle(newTitle);
        }
    }

    public void updateProgress(Player player, double progress) {
        BossBar bossBar = activeBossBars.get(player.getUniqueId());
        if (bossBar != null) {
            bossBar.setProgress(Math.max(0.0, Math.min(1.0, progress)));
        }
    }

    public void startCountdown(Player player, String title, int durationInSeconds, BarColor color, BarStyle style) {
        removeBossBar(player);

        createBossBar(player, title, color, style, 1.0);

        taskUtil.scheduleTask(player, "bossbar_countdown", new Runnable() {
            int timeLeft = durationInSeconds;

            @Override
            public void run() {
                if (timeLeft <= 0) {
                    removeBossBar(player);
                    return;
                }

                double progress = (double) timeLeft / durationInSeconds;
                updateProgress(player, progress);
                updateTitle(player, title + " (" + timeLeft + "s)");

                timeLeft--;
            }
        }, durationInSeconds);
    }

    public boolean hasActiveBossBar(Player player) {
        return activeBossBars.containsKey(player.getUniqueId());
    }
}
