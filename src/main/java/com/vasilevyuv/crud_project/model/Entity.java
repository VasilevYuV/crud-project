package com.vasilevyuv.crud_project.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Entity {
    private UUID id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Конструктор для создания новой сущности (без id)
    public Entity(String name, String description) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.description = description;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    // Конструктор для загрузки из БД (со всеми полями)
    public Entity(UUID id, String name, String description, 
                  LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // Геттеры
    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    
    // Сеттеры (только для полей, которые могут меняться)
    public void setName(String name) { 
        this.name = name; 
        this.updatedAt = LocalDateTime.now();
    }
    
    public void setDescription(String description) { 
        this.description = description; 
        this.updatedAt = LocalDateTime.now();
    }
    
    // Для обновления updatedAt при любых изменениях
    public void updateTimestamps() {
        this.updatedAt = LocalDateTime.now();
    }
    
    @Override
    public String toString() {
        return "Entity{id=" + id + ", name='" + name + "', description='" + description + "'}";
    }
}