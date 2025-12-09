package com.example.medireminder.servlet;

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
import java.util.HashMap;
import java.util.Map;

/**
 * Servlet for marking reminders as taken
 * Endpoint: POST /reminders/markTaken
 */
@WebServlet("/reminders/markTaken")
public class MarkReminderServlet extends HttpServlet {
    private final ReminderService reminderService = new ReminderService();
    private final Gson gson = new Gson();

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

            // Get reminder ID from request
            String reminderIdStr = request.getParameter("id");
            
            if (reminderIdStr == null || reminderIdStr.trim().isEmpty()) {
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Reminder ID is required");
                out.print(gson.toJson(jsonResponse));
                return;
            }

            int reminderId = Integer.parseInt(reminderIdStr);

            // Mark reminder as taken
            boolean success = reminderService.markReminderTaken(reminderId);

            if (success) {
                jsonResponse.put("success", true);
                jsonResponse.put("message", "Reminder marked as taken");
            } else {
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Failed to mark reminder as taken");
            }

        } catch (NumberFormatException e) {
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Invalid reminder ID format");
        } catch (Exception e) {
            jsonResponse.put("success", false);
            jsonResponse.put("message", "An error occurred: " + e.getMessage());
            e.printStackTrace();
        }

        out.print(gson.toJson(jsonResponse));
        out.flush();
    }
}
