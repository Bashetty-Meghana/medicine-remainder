package com.example.medireminder.servlet;

import com.example.medireminder.model.Reminder;
import com.example.medireminder.service.ReminderService;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * NEW SERVLET: Handles due reminders for time-based notifications
 * Endpoint: GET /reminders/due
 * 
 * This servlet:
 * 1. Finds all reminders that are due (time has passed) and not yet notified
 * 2. Returns them as JSON to the frontend
 * 3. Marks them as notified to prevent duplicate notifications
 */
@WebServlet("/reminders/due")
public class DueRemindersServlet extends HttpServlet {
    private final ReminderService reminderService = new ReminderService();
    private final Gson gson = new Gson();

    /**
     * GET - Retrieve due reminders for the logged-in user
     * These are reminders where:
     * - Time has passed or is now
     * - Not taken yet
     * - Not notified yet
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        PrintWriter out = response.getWriter();
        Map<String, Object> jsonResponse = new HashMap<>();

        try {
            // Check if user is logged in
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("userId") == null) {
                jsonResponse.put("success", false);
                jsonResponse.put("message", "User not logged in");
                out.print(gson.toJson(jsonResponse));
                return;
            }

            int userId = (Integer) session.getAttribute("userId");

            // Get all due reminders for this user (not taken, not notified, time passed)
            List<Reminder> dueReminders = reminderService.getDueReminders(userId);

            if (dueReminders != null && !dueReminders.isEmpty()) {
                // Collect reminder IDs to mark as notified
                List<Integer> reminderIds = new ArrayList<>();
                for (Reminder reminder : dueReminders) {
                    reminderIds.add(reminder.getId());
                }

                // Mark all these reminders as notified to prevent duplicate notifications
                reminderService.markRemindersAsNotified(reminderIds);

                jsonResponse.put("success", true);
                jsonResponse.put("reminders", dueReminders);
            } else {
                // No due reminders - return empty list
                jsonResponse.put("success", true);
                jsonResponse.put("reminders", new ArrayList<>());
            }

        } catch (Exception e) {
            jsonResponse.put("success", false);
            jsonResponse.put("message", "An error occurred: " + e.getMessage());
            e.printStackTrace();
        }

        out.print(gson.toJson(jsonResponse));
        out.flush();
    }
}
