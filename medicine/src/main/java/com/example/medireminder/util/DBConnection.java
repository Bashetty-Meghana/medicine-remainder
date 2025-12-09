package com.example.medireminder.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Database Connection Helper Class
 * Manages database connections using JDBC
 * Loads configuration from db.properties file
 */
public class DBConnection {
    private static String DB_URL;
    private static String DB_USERNAME;
    private static String DB_PASSWORD;
    private static String DB_DRIVER;

    // Static block to load database configuration
    static {
        loadDatabaseConfig();
    }

    /**
     * Loads database configuration from db.properties file
     */
    private static void loadDatabaseConfig() {
        Properties props = new Properties();
        try (InputStream input = DBConnection.class.getClassLoader()
                .getResourceAsStream("db.properties")) {
            
            if (input == null) {
                System.err.println("Unable to find db.properties file");
                // Fallback to default values
                DB_URL = "jdbc:mysql://localhost:3306/medireminderdb";
                DB_USERNAME = "root";
                DB_PASSWORD = "";
                DB_DRIVER = "com.mysql.cj.jdbc.Driver";
                return;
            }

            // Load properties from file
            props.load(input);
            DB_URL = props.getProperty("db.url");
            DB_USERNAME = props.getProperty("db.username");
            DB_PASSWORD = props.getProperty("db.password");
            DB_DRIVER = props.getProperty("db.driver");

            // Load MySQL JDBC Driver
            Class.forName(DB_DRIVER);
            System.out.println("Database configuration loaded successfully");

        } catch (IOException e) {
            System.err.println("Error loading database configuration: " + e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Gets a database connection
     * @return Connection object
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        try {
            Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            System.out.println("Database connection established");
            return conn;
        } catch (SQLException e) {
            System.err.println("Failed to create database connection: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Closes a database connection safely
     * @param conn Connection to close
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("Database connection closed");
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Test method to verify database connectivity
     * @return true if connection is successful, false otherwise
     */
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Database connection test failed: " + e.getMessage());
            return false;
        }
    }
}
