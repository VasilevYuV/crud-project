package com.vasilevyuv.crud_project.dao;

import com.vasilevyuv.crud_project.model.Entity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EntityDao {
    // CRUD операции
    Entity save(Entity entity);              // Создать или обновить
    Optional<Entity> findById(UUID id);      // Найти по ID
    List<Entity> findAll();                  // Найти все
    boolean update(Entity entity);           // Обновить
    boolean delete(UUID id);                 // Удалить

    // Дополнительные методы
    int count();                             // Количество записей
    boolean existsById(UUID id);             // Проверка существования
    List<Entity> findAll(int offset, int limit);
    List<Entity> searchByName(String searchTerm);
    List<Entity> searchByName(String searchTerm, int offset, int limit);
    int countByName(String searchTerm);
}