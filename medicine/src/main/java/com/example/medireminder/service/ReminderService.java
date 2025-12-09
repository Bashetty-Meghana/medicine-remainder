package com.example.medireminder.service;

import com.example.medireminder.dao.ReminderDao;
import com.example.medireminder.model.Reminder;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

/**
 * Service layer for Reminder operations
 * Contains business logic for reminder management
 */
public class ReminderService {
    private final ReminderDao reminderDao;

    public ReminderService() {
        this.reminderDao = new ReminderDao();
    }

    /**
     * Add a new reminder for a medicine
     * @param userId User ID
     * @param medicineId Medicine ID
     * @param reminderDate Reminder date
     * @param reminderTime Reminder time
     * @return true if reminder was added successfully, false otherwise
     */
    public boolean addReminder(int userId, int medicineId, Date reminderDate, Time reminderTime) {
        // Validate input
        if (reminderDate == null) {
            System.err.println("Reminder date cannot be null");
            return false;
        }
        
        if (reminderTime == null) {
            System.err.println("Reminder time cannot be null");
            return false;
        }

        // Create reminder object
        Reminder reminder = new Reminder(userId, medicineId, reminderDate, reminderTime);
        
        // Save to database
        return reminderDao.saveReminder(reminder);
    }

    /**
     * Get today's reminders for a user
     * @param userId User ID
     * @return List of Reminder objects
     */
    public List<Reminder> getTodayReminders(int userId) {
        return reminderDao.findTodayRemindersByUserId(userId);
    }

    /**
     * Get all reminders for a user
     * @param userId User ID
     * @return List of Reminder objects
     */
    public List<Reminder> getAllReminders(int userId) {
        return reminderDao.findByUserId(userId);
    }

    /**
     * Mark a reminder as taken
     * @param reminderId Reminder ID
     * @return true if update was successful, false otherwise
     */
    public boolean markReminderTaken(int reminderId) {
        return reminderDao.markTaken(reminderId);
    }

    /**
     * Delete a reminder
     * @param reminderId Reminder ID to delete
     * @return true if deletion was successful, false otherwise
     */
    public boolean deleteReminder(int reminderId) {
        return reminderDao.deleteById(reminderId);
    }

    /**
     * NEW: Get due reminders that need notification
     * Returns reminders where time has passed and user hasn't been notified yet
     * @param userId User ID
     * @return List of due Reminder objects
     */
    public List<Reminder> getDueReminders(int userId) {
        return reminderDao.findDueRemindersForUser(userId);
    }

    /**
     * NEW: Mark reminders as notified to prevent duplicate notifications
     * @param reminderIds List of reminder IDs to mark
     * @return true if update was successful, false otherwise
     */
    public boolean markRemindersAsNotified(List<Integer> reminderIds) {
        return reminderDao.markAsNotified(reminderIds);
    }
}
