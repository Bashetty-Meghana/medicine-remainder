-- ============================================
-- Add Notification Feature to Medicine Reminder
-- This script adds the 'notified' column to track which reminders have been notified
-- ============================================

-- Add 'notified' column to reminders table
ALTER TABLE reminders 
ADD COLUMN notified TINYINT(1) DEFAULT 0 AFTER taken;

-- Add index for faster queries on due reminders
CREATE INDEX idx_reminders_notified ON reminders(notified, reminder_date, reminder_time);

-- Verify the change
-- DESCRIBE reminders;
