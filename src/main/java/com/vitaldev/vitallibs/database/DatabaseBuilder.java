package com.vitaldev.vitallibs.database;

import com.vitaldev.vitallibs.util.ConsoleUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class DatabaseBuilder {
    private final String host;
    private final int port;
    private final String databaseName;
    private final String username;
    private final String password;
    private Connection connection;

    protected abstract void setupTables(Connection connection) throws SQLException;

    public DatabaseBuilder(String host, int port, String databaseName, String username, String password) {
        this.host = host;
        this.port = port;
        this.databaseName = databaseName;
        this.username = username;
        this.password = password;
    }

    public void connect() throws SQLException {
        // Logic for database connection...
        String url = "jdbc:mysql://" + host + ":" + port + "/?useSSL=false";
        try (Connection tempConnection = DriverManager.getConnection(url, username, password)) {
            String createDatabaseSQL = "CREATE DATABASE IF NOT EXISTS " + databaseName;
            try (PreparedStatement statement = tempConnection.prepareStatement(createDatabaseSQL)) {
                statement.executeUpdate();
            }
        }
        String dbUrl = "jdbc:mysql://" + host + ":" + port + "/" + databaseName + "?useSSL=false";
        connection = DriverManager.getConnection(dbUrl, username, password);
        setupTables(connection);
    }

    public void disconnect() {
        if (isConnected()) {
            try {
                connection.close();
            } catch (SQLException exception) {
                ConsoleUtil.sendMessage("&f  | An error occurred while accessing the database: " + exception.getMessage());
            }
        }
    }

    public boolean isConnected() {
        return connection != null;
    }

    public Connection getConnection() {
        return connection;
    }
}