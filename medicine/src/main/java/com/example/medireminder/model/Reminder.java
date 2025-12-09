package com.example.medireminder.model;

import java.sql.Date;
import java.sql.Time;

/**
 * Reminder POJO (Plain Old Java Object)
 * Represents a medicine reminder for a user
 */
public class Reminder {
    private int id;
    private int userId;
    private int medicineId;
    private Date reminderDate;
    private Time reminderTime;
    private boolean taken;
    private boolean notified;  // NEW: Tracks if user has been notified about this reminder
    
    // Additional field for display purposes (not in DB)
    private String medicineName;

    // Constructors
    public Reminder() {
    }

    public Reminder(int userId, int medicineId, Date reminderDate, Time reminderTime) {
        this.userId = userId;
        this.medicineId = medicineId;
        this.reminderDate = reminderDate;
        this.reminderTime = reminderTime;
        this.taken = false;
        this.notified = false;  // NEW: Default to not notified
    }

    public Reminder(int id, int userId, int medicineId, Date reminderDate, Time reminderTime, boolean taken) {
        this.id = id;
        this.userId = userId;
        this.medicineId = medicineId;
        this.reminderDate = reminderDate;
        this.reminderTime = reminderTime;
        this.taken = taken;
        this.notified = false;  // NEW: Default to not notified
    }

    // NEW: Constructor with notified field
    public Reminder(int id, int userId, int medicineId, Date reminderDate, Time reminderTime, boolean taken, boolean notified) {
        this.id = id;
        this.userId = userId;
        this.medicineId = medicineId;
        this.reminderDate = reminderDate;
        this.reminderTime = reminderTime;
        this.taken = taken;
        this.notified = notified;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(int medicineId) {
        this.medicineId = medicineId;
    }

    public Date getReminderDate() {
        return reminderDate;
    }

    public void setReminderDate(Date reminderDate) {
        this.reminderDate = reminderDate;
    }

    public Time getReminderTime() {
        return reminderTime;
    }

    public void setReminderTime(Time reminderTime) {
        this.reminderTime = reminderTime;
    }

    public boolean isTaken() {
        return taken;
    }

    public void setTaken(boolean taken) {
        this.taken = taken;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    // NEW: Getter and Setter for notified field
    public boolean isNotified() {
        return notified;
    }

    public void setNotified(boolean notified) {
        this.notified = notified;
    }

    @Override
    public String toString() {
        return "Reminder{" +
                "id=" + id +
                ", userId=" + userId +
                ", medicineId=" + medicineId +
                ", reminderDate=" + reminderDate +
                ", reminderTime=" + reminderTime +
                ", taken=" + taken +
                ", notified=" + notified +
                ", medicineName='" + medicineName + '\'' +
                '}';
    }
}
