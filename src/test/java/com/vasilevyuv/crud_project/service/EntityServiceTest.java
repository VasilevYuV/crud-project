package com.vasilevyuv.crud_project.service;

import com.vasilevyuv.crud_project.model.Entity;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class EntityServiceTest extends DatabaseTestBase {

    @Test
    void testCreateEntityValid() {
        // Arrange
        EntityService service = new EntityService(entityDao);
        String name = "Test Entity";
        String description = "Test Description";

        // Act
        Entity result = service.createEntity(name, description);

        // Assert
        assertNotNull(result);
        assertEquals(name, result.getName());
        assertEquals(description, result.getDescription());
        assertNotNull(result.getId());
        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getUpdatedAt());

        // Проверяем что сохранилось в БД
        List<Entity> allEntities = service.getAllEntities();
        assertEquals(1, allEntities.size());
        assertEquals(name, allEntities.get(0).getName());
    }


}