package com.example.medireminder.service;

import com.example.medireminder.dao.UserDao;
import com.example.medireminder.model.User;

/**
 * Service layer for User operations
 * Contains business logic for user management
 */
public class UserService {
    private final UserDao userDao;

    public UserService() {
        this.userDao = new UserDao();
    }

    /**
     * Register a new user
     * @param username Username
     * @param password Password
     * @return User object if registration successful, null otherwise
     */
    public User registerUser(String username, String password) {
        // Validate input
        if (username == null || username.trim().isEmpty()) {
            System.err.println("Username cannot be empty");
            return null;
        }
        
        if (password == null || password.trim().isEmpty()) {
            System.err.println("Password cannot be empty");
            return null;
        }

        // Check if username already exists
        if (userDao.usernameExists(username)) {
            System.err.println("Username already exists: " + username);
            return null;
        }

        // Create and save user
        User user = new User(username, password);
        boolean saved = userDao.saveUser(user);
        
        if (saved) {
            // Retrieve the saved user to get the generated ID
            return userDao.findByUsernameAndPassword(username, password);
        }
        
        return null;
    }

    /**
     * Login a user
     * @param username Username
     * @param password Password
     * @return User object if login successful, null otherwise
     */
    public User loginUser(String username, String password) {
        // Validate input
        if (username == null || username.trim().isEmpty()) {
            System.err.println("Username cannot be empty");
            return null;
        }
        
        if (password == null || password.trim().isEmpty()) {
            System.err.println("Password cannot be empty");
            return null;
        }

        // Find user by username and password
        User user = userDao.findByUsernameAndPassword(username, password);
        
        if (user == null) {
            System.err.println("Invalid username or password");
        }
        
        return user;
    }

    /**
     * Get user by ID
     * @param userId User ID
     * @return User object if found, null otherwise
     */
    public User getUserById(int userId) {
        return userDao.findById(userId);
    }
}
