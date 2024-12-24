package com.vitaldev.vitallibs.database;

import com.vitaldev.vitallibs.util.ConsoleUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DatabaseHandler {
    private final Connection connection;
    private final String tableName;
    private final ConcurrentHashMap<UUID, Map<String, Integer>> cache = new ConcurrentHashMap<>();
    private final String uuidColumn;
    final JavaPlugin plugin;

    private Map<String, Integer> defaultValues = new HashMap<>();

    public DatabaseHandler(JavaPlugin plugin, String host, int port, String databaseName, String username, String password, String tableName, String uuidColumn, Map<String, Integer> defaultValues) throws SQLException {
        String dbUrl = "jdbc:mysql://" + host + ":" + port + "/" + databaseName + "?useSSL=false";
        this.connection = DriverManager.getConnection(dbUrl, username, password);
        this.plugin = plugin;
        this.tableName = tableName;
        this.uuidColumn = uuidColumn;
        this.defaultValues = defaultValues;
        setupTable(defaultValues);
    }

    private void setupTable(Map<String, Integer> defaultValues) {
        StringBuilder createTableSQL = new StringBuilder(String.format(
                "CREATE TABLE IF NOT EXISTS %s (" +
                        "%s VARCHAR(36) PRIMARY KEY, ",
                tableName, uuidColumn));

        for (String key : defaultValues.keySet()) {
            createTableSQL.append(key).append(" INT NOT NULL DEFAULT ").append(defaultValues.get(key)).append(", ");
        }

        createTableSQL.setLength(createTableSQL.length() - 2);
        createTableSQL.append(");");

        try (PreparedStatement statement = connection.prepareStatement(createTableSQL.toString())) {
            statement.executeUpdate();
        } catch (SQLException exception) {
            ConsoleUtil.sendMessage("&f  | An error occurred while accessing the database: " + exception.getMessage());
        }
    }

    public void loadPlayer(Player player) {
        UUID uuid = player.getUniqueId();

        new BukkitRunnable() {
            @Override
            public void run() {
                try (PreparedStatement statement = connection.prepareStatement(
                        "SELECT * FROM " + tableName + " WHERE " + uuidColumn + " = ?;")) {

                    statement.setString(1, uuid.toString());
                    try (ResultSet resultSet = statement.executeQuery()) {
                        Map<String, Integer> values = new HashMap<>();

                        if (resultSet.next()) {
                            for (String key : defaultValues.keySet()) {
                                values.put(key, resultSet.getInt(key));
                            }
                        } else {
                            StringBuilder insertSQL = new StringBuilder(
                                    "INSERT INTO " + tableName + " (" + uuidColumn);
                            StringBuilder valuePlaceholders = new StringBuilder(" VALUES (?");

                            List<Object> parameters = new ArrayList<>();
                            parameters.add(uuid.toString());

                            for (String key : defaultValues.keySet()) {
                                insertSQL.append(", ").append(key);
                                valuePlaceholders.append(", ?");
                                parameters.add(defaultValues.get(key));
                                values.put(key, defaultValues.get(key));
                            }

                            insertSQL.append(")").append(valuePlaceholders).append(");");
                            try (PreparedStatement insertStatement = connection.prepareStatement(insertSQL.toString())) {
                                for (int i = 0; i < parameters.size(); i++) {
                                    insertStatement.setObject(i + 1, parameters.get(i));
                                }
                                insertStatement.executeUpdate();
                            }
                        }
                        Bukkit.getScheduler().runTask(plugin, () -> cache.put(uuid, values));
                    }
                } catch (SQLException exception) {
                    ConsoleUtil.sendMessage("&f  | An error occurred while accessing the database: " + exception.getMessage());
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    public void saveAndRemovePlayer(Player player) {
        UUID uuid = player.getUniqueId();
        Map<String, Integer> values = cache.remove(uuid);

        if (values != null) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    try {
                        StringBuilder updateSQL = new StringBuilder("UPDATE " + tableName + " SET ");
                        List<Object> parameters = new ArrayList<>();

                        for (Map.Entry<String, Integer> entry : values.entrySet()) {
                            updateSQL.append(entry.getKey()).append(" = ?, ");
                            parameters.add(entry.getValue());
                        }

                        updateSQL.setLength(updateSQL.length() - 2);
                        updateSQL.append(" WHERE ").append(uuidColumn).append(" = ?;");
                        parameters.add(uuid.toString());

                        try (PreparedStatement statement = connection.prepareStatement(updateSQL.toString())) {
                            for (int i = 0; i < parameters.size(); i++) {
                                statement.setObject(i + 1, parameters.get(i));
                            }
                            statement.executeUpdate();
                        }
                    } catch (SQLException exception) {
                        ConsoleUtil.sendMessage("&f  | An error occurred while accessing the database: " + exception.getMessage());
                    }
                }
            }.runTaskAsynchronously(plugin);
        }
    }

    public void savePlayer(Player player) {
        UUID uuid = player.getUniqueId();
        Map<String, Integer> values = cache.get(uuid);

        if (values != null) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    try {
                        StringBuilder updateSQL = new StringBuilder("UPDATE " + tableName + " SET ");
                        List<Object> parameters = new ArrayList<>();

                        for (Map.Entry<String, Integer> entry : values.entrySet()) {
                            updateSQL.append(entry.getKey()).append(" = ?, ");
                            parameters.add(entry.getValue());
                        }

                        updateSQL.setLength(updateSQL.length() - 2);
                        updateSQL.append(" WHERE ").append(uuidColumn).append(" = ?;");
                        parameters.add(uuid.toString());

                        try (PreparedStatement statement = connection.prepareStatement(updateSQL.toString())) {
                            for (int i = 0; i < parameters.size(); i++) {
                                statement.setObject(i + 1, parameters.get(i));
                            }
                            statement.executeUpdate();
                        }
                    } catch (SQLException exception) {
                        ConsoleUtil.sendMessage("&f  | An error occurred while accessing the database: " + exception.getMessage());
                    }
                }
            }.runTaskAsynchronously(plugin);
        }
    }

    public void setValue(UUID uuid, String key, int value) {
        cache.computeIfAbsent(uuid, k -> new HashMap<>()).put(key, value);
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    String sql = "INSERT INTO " + tableName + " (" + uuidColumn + ", " + key + ") VALUES (?, ?) " +
                            "ON DUPLICATE KEY UPDATE " + key + " = ?;";
                    try (PreparedStatement statement = connection.prepareStatement(sql)) {
                        statement.setString(1, uuid.toString());
                        statement.setInt(2, value);
                        statement.setInt(3, value);
                        statement.executeUpdate();
                    }
                } catch (SQLException exception) {
                    ConsoleUtil.sendMessage("&f  | An error occurred while accessing the database: " + exception.getMessage());
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    public void addValue(UUID uuid, String key, int valueToAdd) {
        int newValue = cache.computeIfAbsent(uuid, k -> new HashMap<>())
                .merge(key, valueToAdd, Integer::sum);

        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    String sql = "INSERT INTO " + tableName + " (" + uuidColumn + ", " + key + ") VALUES (?, ?) " +
                            "ON DUPLICATE KEY UPDATE " + key + " = ?;";
                    try (PreparedStatement statement = connection.prepareStatement(sql)) {
                        statement.setString(1, uuid.toString());
                        statement.setInt(2, newValue);
                        statement.setInt(3, newValue);
                        statement.executeUpdate();
                    }
                } catch (SQLException exception) {
                    ConsoleUtil.sendMessage("&f  | An error occurred while accessing the database: " + exception.getMessage());
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    public void removeValue(UUID uuid, String key, int valueToRemove) {
        int newValue = cache.computeIfAbsent(uuid, k -> new HashMap<>())
                .merge(key, -valueToRemove, (oldValue, delta) -> Math.max(0, oldValue + delta));
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    String sql = "INSERT INTO " + tableName + " (" + uuidColumn + ", " + key + ") VALUES (?, ?) " +
                            "ON DUPLICATE KEY UPDATE " + key + " = ?;";
                    try (PreparedStatement statement = connection.prepareStatement(sql)) {
                        statement.setString(1, uuid.toString());
                        statement.setInt(2, newValue);
                        statement.setInt(3, newValue);
                        statement.executeUpdate();
                    }
                } catch (SQLException exception) {
                    ConsoleUtil.sendMessage("&f  | An error occurred while accessing the database: " + exception.getMessage());
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    public int getValue(UUID uuid, String key) {
        return cache.getOrDefault(uuid, new HashMap<>()).getOrDefault(key, 0);
    }

    public HashMap<UUID, Integer> getAllPlayerValues(String columnKey) {
        HashMap<UUID, Integer> resultMap = new HashMap<>();
        String query = "SELECT " + uuidColumn + ", " + columnKey + " FROM " + tableName + ";";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                UUID uuid = UUID.fromString(resultSet.getString(uuidColumn));
                int value = resultSet.getInt(columnKey);
                resultMap.put(uuid, value);
            }
        } catch (SQLException e) {
            ConsoleUtil.sendMessage("&f  | An error occurred while accessing the database: " + e.getMessage());
        }
        return resultMap;
    }

    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException exception) {
            ConsoleUtil.sendMessage("&f  | An error occurred while accessing the database: " + exception.getMessage());
        }
    }
}