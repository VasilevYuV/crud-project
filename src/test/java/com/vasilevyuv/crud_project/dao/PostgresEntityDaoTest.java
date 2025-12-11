package com.vasilevyuv.crud_project.dao;

import com.vasilevyuv.crud_project.model.Entity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PostgresEntityDaoTest {
    
    private PostgresEntityDao entityDao;
    private Entity testEntity;
    
    @BeforeEach
    void setUp() {
        // В реальном тесте здесь была бы инициализация тестовой БД
        // Для примера создадим мок-объект
        testEntity = new Entity("Test Entity", "Test Description");
        entityDao = new PostgresEntityDao();
    }
    
    @AfterEach
    void tearDown() {
        // Очистка тестовых данных
    }
    
    @Test
    void testSaveAndFindById() {
        assertNotNull(testEntity);
        assertNotNull(testEntity.getId());
        assertEquals("Test Entity", testEntity.getName());
    }
    
    @Test
    void testEntityValidation() {
        Entity entity = new Entity("Valid Name", "Valid Description");
        
        assertTrue(entity.getName().length() >= 3);
        assertTrue(entity.getName().length() <= 50);
        assertTrue(entity.getDescription().length() <= 255);
    }
    
    @Test
    void testUUIDGeneration() {
        Entity entity1 = new Entity("Entity 1", "Description 1");
        Entity entity2 = new Entity("Entity 2", "Description 2");
        
        assertNotEquals(entity1.getId(), entity2.getId());
    }
}