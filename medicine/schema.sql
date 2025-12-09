-- ============================================
-- Medicine Reminder Database Schema
-- Database: MySQL 8.0+
-- ============================================

-- STEP 1: Create the database
-- Run this command first in MySQL:
-- CREATE DATABASE medireminderdb;
-- USE medireminderdb;

-- Drop tables if they exist (for clean setup)
DROP TABLE IF EXISTS reminders;
DROP TABLE IF EXISTS medicines;
DROP TABLE IF EXISTS users;

-- ============================================
-- Table: users
-- Stores user account information
-- ============================================
CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- Table: medicines
-- Stores medicine information for each user
-- ============================================
CREATE TABLE medicines (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    name VARCHAR(100) NOT NULL,
    dosage VARCHAR(50),
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- Table: reminders
-- Stores reminder schedules for medicines
-- ============================================
CREATE TABLE reminders (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    medicine_id INT NOT NULL,
    reminder_date DATE NOT NULL,
    reminder_time TIME NOT NULL,
    taken TINYINT(1) DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (medicine_id) REFERENCES medicines(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- Create indexes for better query performance
-- ============================================
CREATE INDEX idx_medicines_user_id ON medicines(user_id);
CREATE INDEX idx_reminders_user_id ON reminders(user_id);
CREATE INDEX idx_reminders_date ON reminders(reminder_date);
CREATE INDEX idx_reminders_medicine_id ON reminders(medicine_id);

-- ============================================
-- Sample Data (Optional - for testing)
-- ============================================
-- Uncomment below to insert sample data

-- INSERT INTO users (username, password) VALUES 
-- ('testuser', 'password123');

-- INSERT INTO medicines (user_id, name, dosage, notes) VALUES 
-- (1, 'Aspirin', '500mg', 'Take with food'),
-- (1, 'Vitamin D', '1000 IU', 'Morning supplement');

-- INSERT INTO reminders (user_id, medicine_id, reminder_date, reminder_time, taken) VALUES 
-- (1, 1, CURDATE(), '08:00:00', 0),
-- (1, 1, CURDATE(), '20:00:00', 0),
-- (1, 2, CURDATE(), '09:00:00', 0);

-- ============================================
-- Verification Queries
-- ============================================
-- Run these to verify the setup:
-- SHOW TABLES;
-- DESCRIBE users;
-- DESCRIBE medicines;
-- DESCRIBE reminders;
