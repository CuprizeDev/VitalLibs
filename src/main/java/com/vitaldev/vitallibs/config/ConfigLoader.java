package com.vitaldev.vitallibs.config;

import com.vitaldev.vitallibs.util.ConsoleUtil;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class ConfigLoader {

    private final Plugin plugin;
    private final File configFile;

    public ConfigLoader(Plugin plugin, String fileName) {
        this.plugin = plugin;
        this.configFile = new File(plugin.getDataFolder(), fileName);
    }

    public YamlConfiguration loadConfig() {
        if (!configFile.exists()) {
            plugin.saveResource(configFile.getName(), false); // Save default resource if not present
        }
        return YamlConfiguration.loadConfiguration(configFile);
    }

    public void saveConfig(YamlConfiguration config) {
        try {
            config.save(configFile);
        } catch (IOException exception) {
            ConsoleUtil.sendMessage("  &f| Failed to save the configuration: " + exception.getMessage());
        }
    }

    public void reloadConfig(YamlConfiguration config) {
        YamlConfiguration loadedConfig = YamlConfiguration.loadConfiguration(configFile);
        config.setDefaults(loadedConfig);
    }
}