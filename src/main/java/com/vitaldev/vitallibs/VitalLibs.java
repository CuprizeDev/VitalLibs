package com.vitaldev.vitallibs;

import com.vitaldev.vitallibs.inventory.InventoryHandler;
import com.vitaldev.vitallibs.util.ConsoleUtil;
import org.bukkit.plugin.java.JavaPlugin;

public final class VitalLibs extends JavaPlugin {

    @Override
    public void onEnable() {
        ConsoleUtil.sendMessage("  &f|");
        ConsoleUtil.sendMessage("  &f| VitalLibs - Activating Plugin!");
        ConsoleUtil.sendMessage("  &f| Version - 1.7");
        ConsoleUtil.sendMessage("  &f| Vital Development - https://discord.gg/eqyXAH7T2k");
        ConsoleUtil.sendMessage("  &f|");

        getServer().getPluginManager().registerEvents(new InventoryHandler(), this);
    }

    @Override
    public void onDisable() {
        ConsoleUtil.sendMessage("  &f|");
        ConsoleUtil.sendMessage("  &f| VitalLibs - Disabling Plugin!");
        ConsoleUtil.sendMessage("  &f| Need Help? Join the discord below!");
        ConsoleUtil.sendMessage("  &f| Vital Development - https://discord.gg/eqyXAH7T2k");
        ConsoleUtil.sendMessage("  &f|");
        getServer().getPluginManager().disablePlugin(this);
    }
}
