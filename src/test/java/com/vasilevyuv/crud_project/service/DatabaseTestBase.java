package com.vasilevyuv.crud_project.service;

import com.vasilevyuv.crud_project.dao.PostgresEntityDao;
import com.vasilevyuv.crud_project.util.DatabaseConnection;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class DatabaseTestBase {
    protected PostgresEntityDao entityDao;

    private static final String TEST_DB_URL = "jdbc:postgresql://localhost:5432/crud_project";
    private static final String TEST_USERNAME = "postgres";
    private static final String TEST_PASSWORD = "your_password";

    private static Connection connection;

    @BeforeEach
    void setUpDatabase() throws SQLException {
        try {
            // Закрываем предыдущее соединение если оно существует
            if (connection != null && !connection.isClosed()) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    // Игнорируем
                }
            }

            // Создаем новое соединение с тестовой БД
            connection = DriverManager.getConnection(TEST_DB_URL, TEST_USERNAME, TEST_PASSWORD);

            // Отключаем автокоммит для управления транзакциями
            connection.setAutoCommit(false);

            // Устанавливаем тестовое соединение
            DatabaseConnection.setTestConnection(connection);

            // Очищаем и создаем таблицу
            resetDatabase();

            // Инициализируем DAO
            entityDao = new PostgresEntityDao();

        } catch (Exception e) {
            if (connection != null) {
                try {
                    connection.rollback();
                    connection.close();
                } catch (SQLException ex) {
                    // Игнорируем
                }
            }
            throw new RuntimeException("Failed to set up test database", e);
        }
    }

    private void resetDatabase() throws SQLException {
        // Удаляем таблицу если существует
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS entities CASCADE");
        }

        // Создаем таблицу заново
        String createTableSQL = """
            CREATE TABLE entities (
                id UUID PRIMARY KEY,
                name VARCHAR(50) NOT NULL,
                description VARCHAR(255),
                created_at TIMESTAMP NOT NULL,
                updated_at TIMESTAMP NOT NULL
            )
            """;

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTableSQL);
        }

        // Фиксируем изменения
        connection.commit();
    }

    @AfterEach
    void tearDownTest() {
        try {
            if (connection != null && !connection.isClosed()) {
                // Откатываем несохраненные изменения
                try {
                    connection.rollback();
                } catch (SQLException e) {
                    // Игнорируем
                }
            }
        } catch (SQLException e) {
            System.err.println("Failed to rollback transaction: " + e.getMessage());
        }
    }

    @AfterAll
    static void tearDownDatabase() {
        try {
            // Очищаем тестовое соединение
            DatabaseConnection.clearTestConnection();

            // Закрываем соединение
            if (connection != null && !connection.isClosed()) {
                try (Statement stmt = connection.createStatement()) {
                    stmt.execute("DROP TABLE IF EXISTS entities CASCADE");
                }
                connection.close();
                System.out.println("BYE BYE MF!!!");
            }
        } catch (Exception e) {
            System.err.println("Failed to clean test database: " + e.getMessage());
        }
    }
}