package com.vitaldev.vitallibs.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vitaldev.vitallibs.VitalLibs;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;

import static org.bukkit.Bukkit.getLogger;

public class FileUtil {

    public VitalLibs plugin;

    public FileUtil(VitalLibs plugin) {
        this.plugin = plugin;
    }

    public boolean createJsonFile(String fileName) {
        if (!fileName.endsWith(".json")) {
            fileName += ".json";
        }

        File jsonFile = new File(this.plugin.getDataFolder(), fileName);

        try {
            if (!jsonFile.exists()) {
                // Create the directories if they don't exist
                jsonFile.getParentFile().mkdirs();

                // Create a new file
                if (jsonFile.createNewFile()) {
                    // Optionally, write an empty JSON object or initial content
                    try (FileWriter writer = new FileWriter(jsonFile)) {
                        writer.write("{}");  // Writing an empty JSON object as the initial content
                    }
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public java.io.File getJsonFile(String fileName) {
        if (!fileName.endsWith(".json")) {
            fileName += ".json";
        }

        java.io.File jsonFile = new java.io.File(this.plugin.getDataFolder(), fileName);

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

    public boolean setJsonValue(String filePath, String path, Object value) {
        File jsonFile = getJsonFile(filePath);
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

    public boolean createYmlFile(String fileName) {
        if (!fileName.endsWith(".yml")) {
            fileName += ".yml";
        }

        File ymlFile = new File(this.plugin.getDataFolder(), fileName);

        if (!ymlFile.exists()) {
            this.plugin.saveResource(fileName, false);
            return true;
        }

        return false;
    }

    public java.io.File getYmlFile(String fileName) {
        if (!fileName.endsWith(".yml")) {
            fileName += ".yml";
        }

        java.io.File ymlFile = new java.io.File(this.plugin.getDataFolder(), fileName);

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

    public boolean createTxtFile(String fileName) {
        if (!fileName.endsWith(".txt")) {
            fileName += ".txt";
        }

        File txtFile = new File(this.plugin.getDataFolder(), fileName);

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

    public String getTextFromFile(String fileName) {
        if (!fileName.endsWith(".txt")) {
            fileName += ".txt";
        }

        File txtFile = new File(this.plugin.getDataFolder(), fileName);

        if (!txtFile.exists()) {
            System.out.println("File not found: " + fileName);
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

    public boolean createFolder(String folderName) {
        java.io.File folder = new java.io.File(this.plugin.getDataFolder(), folderName);
        if (!folder.exists()) {
            return folder.mkdirs();
        }
        return true;
    }

    public File getFolder(String folderName) {
        File folder = new File(this.plugin.getDataFolder(), folderName);
        if (!folder.exists()) {
            if (!folder.mkdirs()) {
                getLogger().warning("Failed to create folder: " + folder.getPath());
            }
        }
        return folder;
    }
}
