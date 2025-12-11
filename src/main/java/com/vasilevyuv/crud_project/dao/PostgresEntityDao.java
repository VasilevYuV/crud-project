package com.vasilevyuv.crud_project.dao;

import com.vasilevyuv.crud_project.model.Entity;
import com.vasilevyuv.crud_project.util.DatabaseConnection;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PostgresEntityDao implements EntityDao {

    @Override
    public Entity save(Entity entity) {
        String sql = "INSERT INTO entities (id, name, description, created_at, updated_at) VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);

            stmt.setObject(1, entity.getId());
            stmt.setString(2, entity.getName());
            stmt.setString(3, entity.getDescription());
            stmt.setTimestamp(4, Timestamp.valueOf(entity.getCreatedAt()));
            stmt.setTimestamp(5, Timestamp.valueOf(entity.getUpdatedAt()));

            stmt.executeUpdate();
            return entity;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to save entity", e);
        } finally {
            closeStatement(stmt);
            DatabaseConnection.releaseConnection(conn);
        }
    }

    @Override
    public Optional<Entity> findById(UUID id) {
        String sql = "SELECT * FROM entities WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);

            stmt.setObject(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToEntity(rs));
            }
            return Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find entity by id: " + id, e);
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
            DatabaseConnection.releaseConnection(conn);
        }
    }

    @Override
    public List<Entity> findAll() {
        List<Entity> entities = new ArrayList<>();
        String sql = "SELECT * FROM entities ORDER BY created_at DESC";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                entities.add(mapResultSetToEntity(rs));
            }
            return entities;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find all entities", e);
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
            DatabaseConnection.releaseConnection(conn);
        }
    }

    @Override
    public boolean update(Entity entity) {
        String sql = "UPDATE entities SET name = ?, description = ?, updated_at = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);

            stmt.setString(1, entity.getName());
            stmt.setString(2, entity.getDescription());
            stmt.setTimestamp(3, Timestamp.valueOf(entity.getUpdatedAt()));
            stmt.setObject(4, entity.getId());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to update entity: " + entity.getId(), e);
        } finally {
            closeStatement(stmt);
            DatabaseConnection.releaseConnection(conn);
        }
    }

    @Override
    public boolean delete(UUID id) {
        String sql = "DELETE FROM entities WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);

            stmt.setObject(1, id);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete entity: " + id, e);
        } finally {
            closeStatement(stmt);
            DatabaseConnection.releaseConnection(conn);
        }
    }

    @Override
    public int count() {
        String sql = "SELECT COUNT(*) FROM entities";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to count entities", e);
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
            DatabaseConnection.releaseConnection(conn);
        }
    }

    @Override
    public boolean existsById(UUID id) {
        String sql = "SELECT 1 FROM entities WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);

            stmt.setObject(1, id);
            rs = stmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to check if entity exists: " + id, e);
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
            DatabaseConnection.releaseConnection(conn);
        }
    }

    // Вспомогательные методы для закрытия ресурсов
    private void closeStatement(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                // Логируем, но не выбрасываем
                System.err.println("Error closing statement: " + e.getMessage());
            }
        }
    }

    private void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                // Логируем, но не выбрасываем
                System.err.println("Error closing result set: " + e.getMessage());
            }
        }
    }

    // Вспомогательный метод для преобразования ResultSet в Entity
    private Entity mapResultSetToEntity(ResultSet rs) throws SQLException {
        UUID id = (UUID) rs.getObject("id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
        LocalDateTime updatedAt = rs.getTimestamp("updated_at").toLocalDateTime();

        return new Entity(id, name, description, createdAt, updatedAt);
    }

    @Override
    public List<Entity> findAll(int offset, int limit) {
        List<Entity> entities = new ArrayList<>();
        String sql = "SELECT * FROM entities ORDER BY created_at DESC LIMIT ? OFFSET ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, limit);
            stmt.setInt(2, offset);
            rs = stmt.executeQuery();

            while (rs.next()) {
                entities.add(mapResultSetToEntity(rs));
            }
            return entities;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find entities with pagination", e);
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
            DatabaseConnection.releaseConnection(conn);
        }
    }

    @Override
    public List<Entity> searchByName(String searchTerm) {
        List<Entity> entities = new ArrayList<>();
        String sql = "SELECT * FROM entities WHERE LOWER(name) LIKE LOWER(?) ORDER BY created_at DESC";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, "%" + searchTerm + "%");
            rs = stmt.executeQuery();

            while (rs.next()) {
                entities.add(mapResultSetToEntity(rs));
            }
            return entities;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to search entities by name", e);
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
            DatabaseConnection.releaseConnection(conn);
        }
    }

    @Override
    public List<Entity> searchByName(String searchTerm, int offset, int limit) {
        List<Entity> entities = new ArrayList<>();
        String sql = "SELECT * FROM entities WHERE LOWER(name) LIKE LOWER(?) " +
                "ORDER BY created_at DESC LIMIT ? OFFSET ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, "%" + searchTerm + "%");
            stmt.setInt(2, limit);
            stmt.setInt(3, offset);
            rs = stmt.executeQuery();

            while (rs.next()) {
                entities.add(mapResultSetToEntity(rs));
            }
            return entities;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to search entities with pagination", e);
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
            DatabaseConnection.releaseConnection(conn);
        }
    }

    @Override
    public int countByName(String searchTerm) {
        String sql = "SELECT COUNT(*) FROM entities WHERE LOWER(name) LIKE LOWER(?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, "%" + searchTerm + "%");
            rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to count entities by name", e);
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
            DatabaseConnection.releaseConnection(conn);
        }
    }
}