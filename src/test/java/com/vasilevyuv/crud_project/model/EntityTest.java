package com.vasilevyuv.crud_project.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class EntityTest {
    
    @Test
    void testEntityCreation() {
        // Arrange
        String name = "Test Entity";
        String description = "Test Description";
        
        // Act
        Entity entity = new Entity(name, description);
        
        // Assert
        assertNotNull(entity.getId());
        assertEquals(name, entity.getName());
        assertEquals(description, entity.getDescription());
        assertNotNull(entity.getCreatedAt());
        assertNotNull(entity.getUpdatedAt());
        assertTrue(entity.getCreatedAt().isBefore(LocalDateTime.now().plusSeconds(1)));
        assertTrue(entity.getUpdatedAt().isBefore(LocalDateTime.now().plusSeconds(1)));
    }
    
    @Test
    void testEntityFullConstructor() {
        // Arrange
        UUID id = UUID.randomUUID();
        String name = "Test Entity";
        String description = "Test Description";
        LocalDateTime createdAt = LocalDateTime.now().minusDays(1);
        LocalDateTime updatedAt = LocalDateTime.now().minusHours(1);
        
        // Act
        Entity entity = new Entity(id, name, description, createdAt, updatedAt);
        
        // Assert
        assertEquals(id, entity.getId());
        assertEquals(name, entity.getName());
        assertEquals(description, entity.getDescription());
        assertEquals(createdAt, entity.getCreatedAt());
        assertEquals(updatedAt, entity.getUpdatedAt());
    }
    
    @Test
    void testSetNameUpdatesTimestamp() throws InterruptedException {
        // Arrange
        Entity entity = new Entity("Old Name", "Description");
        LocalDateTime oldUpdatedAt = entity.getUpdatedAt();
        
        // Act
        Thread.sleep(10); // Небольшая задержка для различия во времени
        entity.setName("New Name");
        
        // Assert
        assertEquals("New Name", entity.getName());
        assertTrue(entity.getUpdatedAt().isAfter(oldUpdatedAt));
    }
    
    @Test
    void testSetDescriptionUpdatesTimestamp() throws InterruptedException {
        // Arrange
        Entity entity = new Entity("Name", "Old Description");
        LocalDateTime oldUpdatedAt = entity.getUpdatedAt();
        
        // Act
        Thread.sleep(10);
        entity.setDescription("New Description");
        
        // Assert
        assertEquals("New Description", entity.getDescription());
        assertTrue(entity.getUpdatedAt().isAfter(oldUpdatedAt));
    }
    
    @Test
    void testUpdateTimestamps() throws InterruptedException {
        // Arrange
        Entity entity = new Entity("Name", "Description");
        LocalDateTime oldUpdatedAt = entity.getUpdatedAt();
        
        // Act
        Thread.sleep(10);
        entity.updateTimestamps();
        
        // Assert
        assertTrue(entity.getUpdatedAt().isAfter(oldUpdatedAt));
    }
    
    @Test
    void testToString() {
        // Arrange
        Entity entity = new Entity("Test Name", "Test Description");
        
        // Act
        String result = entity.toString();
        
        // Assert
        assertTrue(result.contains("Test Name"));
        assertTrue(result.contains("Test Description"));
        assertTrue(result.contains(entity.getId().toString()));
    }
}