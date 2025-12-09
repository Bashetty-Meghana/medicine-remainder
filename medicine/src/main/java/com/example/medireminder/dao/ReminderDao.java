package com.example.medireminder.dao;

import com.example.medireminder.model.Reminder;
import com.example.medireminder.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Reminder entity
 * Handles all database operations related to reminders
 * Uses PreparedStatement to prevent SQL injection
 */
public class ReminderDao {

    /**
     * Save a new reminder to the database
     * @param reminder Reminder object to save
     * @return true if save was successful, false otherwise
     */
    public boolean saveReminder(Reminder reminder) {
        String sql = "INSERT INTO reminders (user_id, medicine_id, reminder_date, reminder_time, taken) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, reminder.getUserId());
            stmt.setInt(2, reminder.getMedicineId());
            stmt.setDate(3, reminder.getReminderDate());
            stmt.setTime(4, reminder.getReminderTime());
            stmt.setBoolean(5, reminder.isTaken());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error saving reminder: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Find today's reminders for a specific user
     * Joins with medicines table to get medicine names
     * @param userId User ID
     * @return List of Reminder objects with medicine names populated
     */
    public List<Reminder> findTodayRemindersByUserId(int userId) {
        List<Reminder> reminders = new ArrayList<>();
        String sql = "SELECT r.id, r.user_id, r.medicine_id, r.reminder_date, r.reminder_time, r.taken, r.notified, m.name as medicine_name " +
                     "FROM reminders r " +
                     "INNER JOIN medicines m ON r.medicine_id = m.id " +
                     "WHERE r.user_id = ? AND r.reminder_date = CURDATE() " +
                     "ORDER BY r.reminder_time";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Reminder reminder = new Reminder();
                reminder.setId(rs.getInt("id"));
                reminder.setUserId(rs.getInt("user_id"));
                reminder.setMedicineId(rs.getInt("medicine_id"));
                reminder.setReminderDate(rs.getDate("reminder_date"));
                reminder.setReminderTime(rs.getTime("reminder_time"));
                reminder.setTaken(rs.getBoolean("taken"));
                reminder.setNotified(rs.getBoolean("notified"));  // NEW: Load notified status
                reminder.setMedicineName(rs.getString("medicine_name"));
                reminders.add(reminder);
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding today's reminders: " + e.getMessage());
            e.printStackTrace();
        }
        
        return reminders;
    }

    /**
     * Mark a reminder as taken
     * @param reminderId Reminder ID to mark as taken
     * @return true if update was successful, false otherwise
     */
    public boolean markTaken(int reminderId) {
        String sql = "UPDATE reminders SET taken = 1 WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, reminderId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error marking reminder as taken: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Find all reminders for a specific user
     * @param userId User ID
     * @return List of Reminder objects
     */
    public List<Reminder> findByUserId(int userId) {
        List<Reminder> reminders = new ArrayList<>();
        String sql = "SELECT r.id, r.user_id, r.medicine_id, r.reminder_date, r.reminder_time, r.taken, r.notified, m.name as medicine_name " +
                     "FROM reminders r " +
                     "INNER JOIN medicines m ON r.medicine_id = m.id " +
                     "WHERE r.user_id = ? " +
                     "ORDER BY r.reminder_date DESC, r.reminder_time DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Reminder reminder = new Reminder();
                reminder.setId(rs.getInt("id"));
                reminder.setUserId(rs.getInt("user_id"));
                reminder.setMedicineId(rs.getInt("medicine_id"));
                reminder.setReminderDate(rs.getDate("reminder_date"));
                reminder.setReminderTime(rs.getTime("reminder_time"));
                reminder.setTaken(rs.getBoolean("taken"));
                reminder.setNotified(rs.getBoolean("notified"));  // NEW: Load notified status
                reminder.setMedicineName(rs.getString("medicine_name"));
                reminders.add(reminder);
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding reminders: " + e.getMessage());
            e.printStackTrace();
        }
        
        return reminders;
    }

    /**
     * Delete a reminder by ID
     * @param reminderId Reminder ID to delete
     * @return true if deletion was successful, false otherwise
     */
    public boolean deleteById(int reminderId) {
        String sql = "DELETE FROM reminders WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, reminderId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting reminder: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * NEW: Find due (overdue + current) reminders that haven't been notified yet
     * This method finds reminders where:
     * - User matches
     * - Not taken yet
     * - Not notified yet
     * - Reminder date/time has passed or is now
     * @param userId User ID
     * @return List of due Reminder objects with medicine names
     */
    public List<Reminder> findDueRemindersForUser(int userId) {
        List<Reminder> reminders = new ArrayList<>();
        String sql = "SELECT r.id, r.user_id, r.medicine_id, r.reminder_date, r.reminder_time, r.taken, r.notified, m.name as medicine_name " +
                     "FROM reminders r " +
                     "INNER JOIN medicines m ON r.medicine_id = m.id " +
                     "WHERE r.user_id = ? " +
                     "AND r.taken = 0 " +
                     "AND r.notified = 0 " +
                     "AND (r.reminder_date < CURDATE() OR " +
                     "     (r.reminder_date = CURDATE() AND r.reminder_time <= CURTIME())) " +
                     "ORDER BY r.reminder_date, r.reminder_time";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Reminder reminder = new Reminder();
                reminder.setId(rs.getInt("id"));
                reminder.setUserId(rs.getInt("user_id"));
                reminder.setMedicineId(rs.getInt("medicine_id"));
                reminder.setReminderDate(rs.getDate("reminder_date"));
                reminder.setReminderTime(rs.getTime("reminder_time"));
                reminder.setTaken(rs.getBoolean("taken"));
                reminder.setNotified(rs.getBoolean("notified"));
                reminder.setMedicineName(rs.getString("medicine_name"));
                reminders.add(reminder);
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding due reminders: " + e.getMessage());
            e.printStackTrace();
        }
        
        return reminders;
    }

    /**
     * NEW: Mark reminders as notified
     * This prevents duplicate notifications
     * @param reminderIds List of reminder IDs to mark as notified
     * @return true if update was successful, false otherwise
     */
    public boolean markAsNotified(List<Integer> reminderIds) {
        if (reminderIds == null || reminderIds.isEmpty()) {
            return true; // Nothing to update
        }
        
        // Build IN clause with placeholders
        StringBuilder sql = new StringBuilder("UPDATE reminders SET notified = 1 WHERE id IN (");
        for (int i = 0; i < reminderIds.size(); i++) {
            sql.append("?");
            if (i < reminderIds.size() - 1) {
                sql.append(", ");
            }
        }
        sql.append(")");
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            
            // Set all reminder IDs as parameters
            for (int i = 0; i < reminderIds.size(); i++) {
                stmt.setInt(i + 1, reminderIds.get(i));
            }
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error marking reminders as notified: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
