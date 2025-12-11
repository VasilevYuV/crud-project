package com.vasilevyuv.crud_project.config;

public class DatabaseConfig {
    
    public static String getUrl() {
        return EnvConfig.get("DB_URL", "jdbc:postgresql://localhost:5432/crud_project");
    }
    
    public static String getUsername() {
        return EnvConfig.get("DB_USERNAME", "postgres");
    }
    
    public static String getPassword() {
        return EnvConfig.get("DB_PASSWORD", "postgres");
    }
    
    public static int getPoolSize() {
        try {
            return Integer.parseInt(EnvConfig.get("DB_POOL_SIZE", "10"));
        } catch (NumberFormatException e) {
            return 10; // значение по умолчанию
        }
    }
    
    public static int getPoolTimeout() {
        try {
            return Integer.parseInt(EnvConfig.get("DB_POOL_TIMEOUT", "30000"));
        } catch (NumberFormatException e) {
            return 30000; // значение по умолчанию
        }
    }
}