package com.vitaldev.vitallibs.config;

import com.vitaldev.vitallibs.util.ChatUtil;
import com.vitaldev.vitallibs.util.ConsoleUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public class ConfigHandler {
    private final File file;
    private final FileConfiguration config;
    private final Plugin plugin;

    private static final String DEFAULT_MESSAGE = "Default message";
    private static final boolean DEFAULT_ENABLED = true;
    private static final int DEFAULT_TIMEOUT = 30;

    public ConfigHandler(Plugin plugin, File file) {
        this.plugin = plugin;
        this.file = file;

        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                plugin.saveResource(file.getName(), false);
            } catch (IllegalArgumentException e) {
                try {
                    file.createNewFile();
                } catch (IOException exception) {
                    ConsoleUtil.sendMessage("  &f| Could not create config file: " + file.getName());
                }
            }
        }

        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public String getMessage(String key) {
        return ChatUtil.color(config.getString(key, DEFAULT_MESSAGE));
    }

    public String getString(String key) {
        return config.getString(key, DEFAULT_MESSAGE);
    }

    public List<String> getStringList(String key) {
        return config.getStringList(key);
    }

    public boolean contains(String key) {
        return config.contains(key);
    }

    public void set(String key, Object value) {
        config.set(key, value);
    }

    public int getInt(String key) {
        return config.getInt(key, DEFAULT_TIMEOUT);
    }

    public List<Integer> getIntegerList(String key) {
        return config.getIntegerList(key);
    }

    public boolean getBoolean(String key) {
        return config.getBoolean(key, DEFAULT_ENABLED);
    }

    public void createSection(String path) {
        this.config.createSection(path);
    }

    public ConfigurationSection getConfigurationSection(String path) {
        return this.config.getConfigurationSection(path);
    }

    public long getLong(String path) {
        return this.config.getLong(path);
    }

    public double getDouble(String path) {
        return this.config.getDouble(path);
    }

    public double getDouble(String path, double def) {
        return this.config.getDouble(path, def);
    }

    public List<Double> getDoubleList(String path) {
        return this.config.getDoubleList(path);
    }

    public List<?> getList(String path) {
        return this.config.getList(path);
    }

    public List<?> getList(String path, List<?> def) {
        return this.config.getList(path, def);
    }

    public void removeKey(String path) {
        this.config.set(path, null);
    }

    public byte getByte(String path) {
        return (byte)getInt(path);
    }

    public short getShort(String path) {
        return (short)getInt(path);
    }

    public Set<String> getKeys() {
        return this.config.getKeys(false);
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException exception) {
            ConsoleUtil.sendMessage("  &f| Could not save config file: " + file.getName());
        }
    }

    public void reload() {
        try {
            config.load(file);
        } catch (Exception exception) {
            ConsoleUtil.sendMessage("  &f| Could not reload config file: " + file.getName());
        }
    }

    public File getFile() {
        return this.file;
    }

    public String getFileName() {
        return this.file.getName();
    }
}
