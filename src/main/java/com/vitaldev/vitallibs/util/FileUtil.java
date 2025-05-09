package com.vitaldev.vitallibs.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.*;

public class FileUtil {


    public boolean createJsonFile(Plugin plugin, String fileName) {
        if (!fileName.endsWith(".json")) {
            fileName += ".json";
        }
        File jsonFile = new File(plugin.getDataFolder(), fileName);
        try {
            if (!jsonFile.exists()) {
                jsonFile.getParentFile().mkdirs();
                if (jsonFile.createNewFile()) {
                    try (FileWriter writer = new FileWriter(jsonFile)) {
                        writer.write("{}");
                    }
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public java.io.File getJsonFile(Plugin plugin, String fileName) {
        if (!fileName.endsWith(".json")) {
            fileName += ".json";
        }

        java.io.File jsonFile = new java.io.File(plugin.getDataFolder(), fileName);

        if (!jsonFile.exists()) {
            try {
                if (jsonFile.getParentFile().mkdirs() || jsonFile.createNewFile()) {
                    FileWriter writer = new FileWriter(jsonFile);
                    writer.write("{}");
                    writer.flush();
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return jsonFile;
    }

    public boolean setJsonValue(Plugin plugin, String filePath, String path, Object value) {
        File jsonFile = getJsonFile(plugin ,filePath);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        JsonObject jsonObject = jsonFile.exists() ? parseJson(jsonFile) : new JsonObject();
        if (jsonObject == null) return false;

        JsonObject current = jsonObject;
        String[] keys = path.split("\\.");
        for (int i = 0; i < keys.length - 1; i++) {
            current = current.has(keys[i]) ? current.getAsJsonObject(keys[i]) : current;
        }

        current.addProperty(keys[keys.length - 1], String.valueOf(value));

        return writeJsonToFile(jsonFile, gson.toJson(jsonObject));
    }

    private JsonObject parseJson(File jsonFile) {
        try (FileReader reader = new FileReader(jsonFile)) {
            return JsonParser.parseReader(reader).getAsJsonObject();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean writeJsonToFile(File jsonFile, String jsonContent) {
        try (FileWriter writer = new FileWriter(jsonFile)) {
            writer.write(jsonContent);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean createYmlFile(Plugin plugin, String fileName) {
        if (!fileName.endsWith(".yml")) {
            fileName += ".yml";
        }

        File ymlFile = new File(plugin.getDataFolder(), fileName);

        if (!ymlFile.exists()) {
            plugin.saveResource(fileName, false);
            return true;
        }

        return false;
    }

    public java.io.File getYmlFile(Plugin plugin, String fileName) {
        if (!fileName.endsWith(".yml")) {
            fileName += ".yml";
        }

        java.io.File ymlFile = new java.io.File(plugin.getDataFolder(), fileName);

        if (!ymlFile.exists()) {
            try {
                if (ymlFile.getParentFile().mkdirs() || ymlFile.createNewFile()) {
                    YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(ymlFile);
                    defaultConfig.save(ymlFile);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return ymlFile;
    }

    public boolean createTxtFile(Plugin plugin, String fileName) {
        if (!fileName.endsWith(".txt")) {
            fileName += ".txt";
        }

        File txtFile = new File(plugin.getDataFolder(), fileName);

        if (!txtFile.exists()) {
            try {
                txtFile.createNewFile();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    public String getTextFromFile(Plugin plugin, String fileName) {
        if (!fileName.endsWith(".txt")) {
            fileName += ".txt";
        }

        File txtFile = new File(plugin.getDataFolder(), fileName);

        if (!txtFile.exists()) {
            ConsoleUtil.sendMessage("  &f| File not found: " + fileName);
            return null;
        }

        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(txtFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }

    public boolean createFolder(Plugin plugin, String folderName) {
        java.io.File folder = new java.io.File(plugin.getDataFolder(), folderName);
        if (!folder.exists()) {
            return folder.mkdirs();
        }
        return true;
    }

    public File getFolder(Plugin plugin, String folderName) {
        File folder = new File(plugin.getDataFolder(), folderName);
        if (!folder.exists()) {
            if (!folder.mkdirs()) {
                ConsoleUtil.sendMessage("  &f| Failed to create folder: " + folder.getPath());
            }
        }
        return folder;
    }
}
