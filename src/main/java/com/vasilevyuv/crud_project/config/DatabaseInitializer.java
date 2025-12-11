package com.vasilevyuv.crud_project.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DatabaseInitializer {
    public static void initializeDatabase() {
        System.out.println("Initializing database...");
        
        try (Connection conn = DriverManager.getConnection(
                DatabaseConfig.getUrl(),
                DatabaseConfig.getUsername(),
                DatabaseConfig.getPassword());
             Statement stmt = conn.createStatement()) {
            Class.forName("org.postgresql.Driver");

            String createTableSQL = 
                "CREATE TABLE IF NOT EXISTS entities (" +
                "    id UUID PRIMARY KEY DEFAULT gen_random_uuid()," +
                "    name VARCHAR(50) NOT NULL CHECK (LENGTH(name) >= 3)," +
                "    description VARCHAR(255)," +
                "    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ");";
            
            stmt.execute(createTableSQL);

            String createIndexSQL = 
                "CREATE INDEX IF NOT EXISTS idx_entities_name ON entities(name);";
            stmt.execute(createIndexSQL);
            
            System.out.println("Database initialized successfully");
            
        } catch (Exception e) {
            System.err.println("Failed to initialize database: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Database initialization failed", e);
        }
    }
    
    public static void checkDatabaseConnection() {
        try (Connection conn = DriverManager.getConnection(
                DatabaseConfig.getUrl(),
                DatabaseConfig.getUsername(),
                DatabaseConfig.getPassword())) {
            
            System.out.println("Database connection test: SUCCESS");
            return;
            
        } catch (Exception e) {
            System.err.println("Database connection test: FAILED - " + e.getMessage());
            throw new RuntimeException("Cannot connect to database", e);
        }
    }
}