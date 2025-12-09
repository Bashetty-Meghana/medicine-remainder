package com.example.medireminder.servlet;

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
import java.util.Map;

/**
 * Servlet for deleting medicines
 * Endpoint: POST /medicines/delete
 */
@WebServlet("/medicines/delete")
public class DeleteMedicineServlet extends HttpServlet {
    private final MedicineService medicineService = new MedicineService();
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

            // Get medicine ID from request
            String medicineIdStr = request.getParameter("id");
            
            if (medicineIdStr == null || medicineIdStr.trim().isEmpty()) {
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Medicine ID is required");
                out.print(gson.toJson(jsonResponse));
                return;
            }

            int medicineId = Integer.parseInt(medicineIdStr);

            // Delete medicine
            boolean success = medicineService.deleteMedicine(medicineId);

            if (success) {
                jsonResponse.put("success", true);
                jsonResponse.put("message", "Medicine deleted successfully");
            } else {
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Failed to delete medicine");
            }

        } catch (NumberFormatException e) {
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Invalid medicine ID format");
        } catch (Exception e) {
            jsonResponse.put("success", false);
            jsonResponse.put("message", "An error occurred: " + e.getMessage());
            e.printStackTrace();
        }

        out.print(gson.toJson(jsonResponse));
        out.flush();
    }
}
