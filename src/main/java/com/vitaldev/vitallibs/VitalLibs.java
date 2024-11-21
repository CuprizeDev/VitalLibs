package com.vitaldev.vitallibs;

import com.vitaldev.vitallibs.util.ConsoleUtil;
import com.vitaldev.vitallibs.util.FileUtil;
import dev.respark.licensegate.LicenseGate;
import org.bukkit.plugin.java.JavaPlugin;

public final class VitalLibs extends JavaPlugin {

    @Override
    public void onEnable() {
        ConsoleUtil.sendMessage("  &f|");
        ConsoleUtil.sendMessage("  &f| VitalLibs - Activating Plugin!");
        ConsoleUtil.sendMessage("  &f| Version - 1.0.0");
        ConsoleUtil.sendMessage("  &f| Vital Development - https://discord.gg/eqyXAH7T2k");
        ConsoleUtil.sendMessage("  &f|");
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
