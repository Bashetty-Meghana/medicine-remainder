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
import java.sql.Date;
import java.sql.Time;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servlet for reminder operations
 * Endpoints:
 * - GET /reminders/today - get today's reminders
 * - POST /reminders - add a new reminder
 */
@WebServlet("/reminders")
public class ReminderServlet extends HttpServlet {
    private final ReminderService reminderService = new ReminderService();
    private final Gson gson = new Gson();

    /**
     * GET - Retrieve today's reminders for the logged-in user
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

            // Get today's reminders for this user
            List<Reminder> reminders = reminderService.getTodayReminders(userId);

            jsonResponse.put("success", true);
            jsonResponse.put("reminders", reminders);

        } catch (Exception e) {
            jsonResponse.put("success", false);
            jsonResponse.put("message", "An error occurred: " + e.getMessage());
            e.printStackTrace();
        }

        out.print(gson.toJson(jsonResponse));
        out.flush();
    }

    /**
     * POST - Add a new reminder
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
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

            // Get reminder details from request
            String medicineIdStr = request.getParameter("medicineId");
            String reminderDateStr = request.getParameter("reminderDate");
            String reminderTimeStr = request.getParameter("reminderTime");

            // Validate input
            if (medicineIdStr == null || medicineIdStr.trim().isEmpty() ||
                reminderDateStr == null || reminderDateStr.trim().isEmpty() ||
                reminderTimeStr == null || reminderTimeStr.trim().isEmpty()) {
                jsonResponse.put("success", false);
                jsonResponse.put("message", "All fields are required");
                out.print(gson.toJson(jsonResponse));
                return;
            }

            int medicineId = Integer.parseInt(medicineIdStr);
            Date reminderDate = Date.valueOf(reminderDateStr);
            Time reminderTime = Time.valueOf(reminderTimeStr + ":00");

            // Add reminder
            boolean success = reminderService.addReminder(userId, medicineId, reminderDate, reminderTime);

            if (success) {
                jsonResponse.put("success", true);
                jsonResponse.put("message", "Reminder added successfully");
            } else {
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Failed to add reminder");
            }

        } catch (NumberFormatException e) {
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Invalid medicine ID format");
        } catch (IllegalArgumentException e) {
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Invalid date or time format");
        } catch (Exception e) {
            jsonResponse.put("success", false);
            jsonResponse.put("message", "An error occurred: " + e.getMessage());
            e.printStackTrace();
        }

        out.print(gson.toJson(jsonResponse));
        out.flush();
    }
}
