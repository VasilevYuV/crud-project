package com.vasilevyuv.crud_project.service;

import com.vasilevyuv.crud_project.dao.EntityDao;
import com.vasilevyuv.crud_project.dao.PostgresEntityDao;
import com.vasilevyuv.crud_project.model.Entity;
import java.util.List;
import java.util.UUID;

public class EntityService {
    private final EntityDao entityDao;
    
    public EntityService() {
        this.entityDao = new PostgresEntityDao();
    }
    
    public EntityService(EntityDao entityDao) {
        this.entityDao = entityDao;
    }
    
    public Entity createEntity(String name, String description) {
        validateName(name);
        validateDescription(description);
        
        Entity entity = new Entity(name, description);
        return entityDao.save(entity);
    }
    
    public Entity updateEntity(UUID id, String name, String description) {
        validateName(name);
        validateDescription(description);
        
        Entity entity = entityDao.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Entity not found with id: " + id));
        
        entity.setName(name);
        entity.setDescription(description);
        entity.updateTimestamps();
        
        boolean updated = entityDao.update(entity);
        if (!updated) {
            throw new RuntimeException("Failed to update entity");
        }
        
        return entity;
    }
    
    public boolean deleteEntity(UUID id) {
        if (!entityDao.existsById(id)) {
            throw new IllegalArgumentException("Entity not found with id: " + id);
        }
        
        return entityDao.delete(id);
    }
    
    public List<Entity> getAllEntities() {
        return entityDao.findAll();
    }
    
    public Entity getEntityById(UUID id) {
        return entityDao.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Entity not found with id: " + id));
    }

    public List<Entity> getEntities(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        return entityDao.findAll(offset, pageSize);
    }

    public List<Entity> searchEntities(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return entityDao.findAll();
        }
        return entityDao.searchByName(searchTerm.trim());
    }

    public List<Entity> searchEntities(String searchTerm, int page, int pageSize) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getEntities(page, pageSize);
        }
        int offset = (page - 1) * pageSize;
        return entityDao.searchByName(searchTerm.trim(), offset, pageSize);
    }

    public int getTotalCount() {
        return entityDao.count();
    }

    public int getSearchCount(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getTotalCount();
        }
        return entityDao.countByName(searchTerm.trim());
    }
    
    public int getEntityCount() {
        return entityDao.count();
    }
    
    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        
        if (name.length() < 3) {
            throw new IllegalArgumentException("Name must be at least 3 characters");
        }
        
        if (name.length() > 50) {
            throw new IllegalArgumentException("Name must be at most 50 characters");
        }
    }
    
    private void validateDescription(String description) {
        if (description != null && description.length() > 255) {
            throw new IllegalArgumentException("Description must be at most 255 characters");
        }
    }
}