package com.vitaldev.vitallibs.database;

import com.vitaldev.vitallibs.util.ConsoleUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class DatabaseHandler {
    private final Connection connection;
    private final String tableName;
    private final ConcurrentHashMap<UUID, Integer> cache = new ConcurrentHashMap<>();
    private final String uuidColumn;
    private final String valueColumn;
    private final JavaPlugin plugin;

    public DatabaseHandler(JavaPlugin plugin, String host, int port, String databaseName, String username, String password, String tableName, String uuidColumn, String valueColumn) throws SQLException {
        String dbUrl = "jdbc:mysql://" + host + ":" + port + "/" + databaseName + "?useSSL=false";
        this.connection = DriverManager.getConnection(dbUrl, username, password);
        this.plugin = plugin;
        this.tableName = tableName;
        this.uuidColumn = uuidColumn;
        this.valueColumn = valueColumn;
        setupTable();
    }

    private void setupTable() {
        String createTableSQL = String.format(
                "CREATE TABLE IF NOT EXISTS %s (" +
                        "%s VARCHAR(36) PRIMARY KEY, " +
                        "%s INT NOT NULL DEFAULT 0);", tableName, uuidColumn, valueColumn);
        try (PreparedStatement statement = connection.prepareStatement(createTableSQL)) {
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
                        "SELECT " + valueColumn + " FROM " + tableName + " WHERE " + uuidColumn + " = ?;")) {

                    statement.setString(1, uuid.toString());
                    try (ResultSet resultSet = statement.executeQuery()) {
                        int value = 0;
                        if (resultSet.next()) {
                            value = resultSet.getInt(valueColumn);
                        } else {
                            try (PreparedStatement insertStatement = connection.prepareStatement(
                                    "INSERT INTO " + tableName + " (" + uuidColumn + ", " + valueColumn + ") VALUES (?, ?);")) {
                                insertStatement.setString(1, uuid.toString());
                                insertStatement.setInt(2, 0);
                                insertStatement.executeUpdate();
                            }
                        }

                        final int finalValue = value;
                        Bukkit.getScheduler().runTask(plugin, () -> cache.put(uuid, finalValue));
                    }
                } catch (SQLException exception) {
                    ConsoleUtil.sendMessage("&f  | An error occurred while accessing the database: " + exception.getMessage());
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    public void saveAndRemovePlayer(Player player) {
        UUID uuid = player.getUniqueId();
        Integer value = cache.remove(uuid);

        if (value != null) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    try {
                        PreparedStatement statement = connection.prepareStatement(
                                "UPDATE " + tableName + " SET " + valueColumn + " = ? WHERE " + uuidColumn + " = ?;");
                        statement.setInt(1, value);
                        statement.setString(2, uuid.toString());
                        statement.executeUpdate();
                    } catch (SQLException exception) {
                        ConsoleUtil.sendMessage("&f  | An error occurred while accessing the database: " + exception.getMessage());
                    }
                }
            }.runTaskAsynchronously(plugin);
        }
    }

    public void savePlayer(Player player) {
        UUID uuid = player.getUniqueId();
        Integer value = cache.get(uuid);
        if (value != null) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    try {
                        PreparedStatement statement = connection.prepareStatement(
                                "UPDATE " + tableName + " SET " + valueColumn + " = ? WHERE " + uuidColumn + " = ?;");
                        statement.setInt(1, value);
                        statement.setString(2, uuid.toString());
                        statement.executeUpdate();
                    } catch (SQLException exception) {
                        ConsoleUtil.sendMessage("&f  | An error occurred while accessing the database: " + exception.getMessage());
                    }
                }
            }.runTaskAsynchronously(plugin);
        }
    }

    public void setValue(UUID uuid, int value) {
        cache.put(uuid, value);
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    PreparedStatement statement = connection.prepareStatement(
                            "INSERT INTO " + tableName + " (" + uuidColumn + ", " + valueColumn + ") VALUES (?, ?) " +
                                    "ON DUPLICATE KEY UPDATE " + valueColumn + " = ?;");
                    statement.setString(1, uuid.toString());
                    statement.setInt(2, value);
                    statement.setInt(3, value);
                    statement.executeUpdate();
                } catch (SQLException exception) {
                    ConsoleUtil.sendMessage("&f  | An error occurred while accessing the database: " + exception.getMessage());
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    public void addValue(UUID uuid, int value) {
        int newValue = getValue(uuid) + value;
        cache.put(uuid, newValue);
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    PreparedStatement statement = connection.prepareStatement(
                            "INSERT INTO " + tableName + " (" + uuidColumn + ", " + valueColumn + ") VALUES (?, ?) " +
                                    "ON DUPLICATE KEY UPDATE " + valueColumn + " = ?;");
                    statement.setString(1, uuid.toString());
                    statement.setInt(2, newValue);
                    statement.setInt(3, newValue);
                    statement.executeUpdate();
                } catch (SQLException exception) {
                    ConsoleUtil.sendMessage("&f  | An error occurred while accessing the database: " + exception.getMessage());
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    public void removeValue(UUID uuid, int value) {
        int newValue = getValue(uuid) - value;
        cache.put(uuid, newValue);
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    PreparedStatement statement = connection.prepareStatement(
                            "INSERT INTO " + tableName + " (" + uuidColumn + ", " + valueColumn + ") VALUES (?, ?) " +
                                    "ON DUPLICATE KEY UPDATE " + valueColumn + " = ?;");
                    statement.setString(1, uuid.toString());
                    statement.setInt(2, newValue);
                    statement.setInt(3, newValue);
                    statement.executeUpdate();
                } catch (SQLException exception) {
                    ConsoleUtil.sendMessage("&f  | An error occurred while accessing the database: " + exception.getMessage());
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    public int getValue(UUID uuid) {
        return cache.getOrDefault(uuid, 0);
    }

    public HashMap<UUID, Integer> getAllPlayerValues() {
        HashMap<UUID, Integer> resultMap = new HashMap<>();
        String query = "SELECT " + uuidColumn + ", " + valueColumn + " FROM " + tableName + ";";

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                UUID uuid = UUID.fromString(resultSet.getString(uuidColumn));
                int value = resultSet.getInt(valueColumn);
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