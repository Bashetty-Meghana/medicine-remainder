package com.example.medireminder.servlet;

import com.example.medireminder.model.Medicine;
import com.example.medireminder.service.MedicineService;
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
import java.util.List;
import java.util.Map;

/**
 * Servlet for medicine operations
 * Endpoints:
 * - GET /medicines - get all medicines for logged-in user
 * - POST /medicines - add a new medicine
 */
@WebServlet("/medicines")
public class MedicineServlet extends HttpServlet {
    private final MedicineService medicineService = new MedicineService();
    private final Gson gson = new Gson();

    /**
     * GET - Retrieve all medicines for the logged-in user
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

            // Get all medicines for this user
            List<Medicine> medicines = medicineService.getUserMedicines(userId);

            jsonResponse.put("success", true);
            jsonResponse.put("medicines", medicines);

        } catch (Exception e) {
            jsonResponse.put("success", false);
            jsonResponse.put("message", "An error occurred: " + e.getMessage());
            e.printStackTrace();
        }

        out.print(gson.toJson(jsonResponse));
        out.flush();
    }

    /**
     * POST - Add a new medicine
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

            // Get medicine details from request
            String name = request.getParameter("name");
            String dosage = request.getParameter("dosage");
            String notes = request.getParameter("notes");

            // Validate input
            if (name == null || name.trim().isEmpty()) {
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Medicine name is required");
                out.print(gson.toJson(jsonResponse));
                return;
            }

            // Add medicine
            boolean success = medicineService.addMedicine(userId, name, dosage, notes);

            if (success) {
                jsonResponse.put("success", true);
                jsonResponse.put("message", "Medicine added successfully");
            } else {
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Failed to add medicine");
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
