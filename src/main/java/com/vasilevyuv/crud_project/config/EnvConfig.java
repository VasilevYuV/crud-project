package com.vasilevyuv.crud_project.config;

import io.github.cdimascio.dotenv.Dotenv;

public class EnvConfig {
    private static final Dotenv dotenv;
    
    static {
        dotenv = Dotenv.configure()
                .directory(System.getProperty("user.dir"))
                .ignoreIfMissing()
                .load();
    }
    
    public static String get(String key) {
        // Попытка получить из системных переменных (для prod)
        String value = System.getenv(key);
        if (value != null && !value.isEmpty()) {
            return value;
        }
        // Если нет в системных, то из .env.local файла
        return dotenv.get(key);
    }
    
    public static String get(String key, String defaultValue) {
        String value = get(key);
        return value != null ? value : defaultValue;
    }
}