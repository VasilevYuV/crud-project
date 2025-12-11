package com.vasilevyuv.crud_project.util;

import com.vasilevyuv.crud_project.config.DatabaseConfig;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class DatabaseConnection {
    private static BlockingQueue<Connection> connectionPool;
    private static Connection testConnection = null;

    static {
        initializeConnectionPool();
    }

    public static void setTestConnection(Connection connection) {
        testConnection = connection;
    }

    // Для тестов - очистить тестовое соединение
    public static void clearTestConnection() {
        testConnection = null;
    }

    private static void initializeConnectionPool() {
        int poolSize = DatabaseConfig.getPoolSize();
        connectionPool = new ArrayBlockingQueue<>(poolSize);

        try {
            for (int i = 0; i < poolSize; i++) {
                Connection connection = createConnection();
                connectionPool.offer(connection);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize connection pool", e);
        }
    }

    private static Connection createConnection() throws SQLException {
        return DriverManager.getConnection(
            DatabaseConfig.getUrl(),
            DatabaseConfig.getUsername(),
            DatabaseConfig.getPassword()
        );
    }

    public static Connection getConnection() throws SQLException {
        if (testConnection != null) {
            return testConnection;
        }
        try {
            Connection connection = connectionPool.take();

            // Проверка на соединение (таймаут)
            if (connection.isClosed() || !connection.isValid(2)) { // 2 секунды на проверку
                connection = createConnection();
            }

            return connection;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new SQLException("Interrupted while waiting for connection", e);
        }
    }

    public static void releaseConnection(Connection connection) {
        if (connection != null) {
            connectionPool.offer(connection);
        }
    }

    public static void closeAllConnections() {
        for (Connection connection : connectionPool) {
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
        connectionPool.clear();
    }
}