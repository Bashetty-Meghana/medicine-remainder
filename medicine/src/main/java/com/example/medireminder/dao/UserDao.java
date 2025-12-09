package com.example.medireminder.dao;

import com.example.medireminder.model.User;
import com.example.medireminder.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Data Access Object for User entity
 * Handles all database operations related to users
 * Uses PreparedStatement to prevent SQL injection
 */
public class UserDao {

    /**
     * Check if a username already exists in the database
     * @param username Username to check
     * @return true if username exists, false otherwise
     */
    public boolean usernameExists(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("Error checking username existence: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }

    /**
     * Save a new user to the database
     * @param user User object to save
     * @return true if save was successful, false otherwise
     */
    public boolean saveUser(User user) {
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error saving user: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Find a user by username and password (for login)
     * @param username Username
     * @param password Password
     * @return User object if found, null otherwise
     */
    public User findByUsernameAndPassword(String username, String password) {
        String sql = "SELECT id, username, password FROM users WHERE username = ? AND password = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            stmt.setString(2, password);
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                return user;
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding user: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }

    /**
     * Find a user by ID
     * @param userId User ID
     * @return User object if found, null otherwise
     */
    public User findById(int userId) {
        String sql = "SELECT id, username, password FROM users WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                return user;
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding user by ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
}
