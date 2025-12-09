package com.example.medireminder.servlet;

import com.example.medireminder.model.User;
import com.example.medireminder.service.UserService;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Servlet for user registration
 * Endpoint: POST /register
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private final UserService userService = new UserService();
    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Set response type to JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        PrintWriter out = response.getWriter();
        Map<String, Object> jsonResponse = new HashMap<>();

        try {
            // Get username and password from request
            String username = request.getParameter("username");
            String password = request.getParameter("password");

            // Validate input
            if (username == null || username.trim().isEmpty() ||
                password == null || password.trim().isEmpty()) {
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Username and password are required");
                out.print(gson.toJson(jsonResponse));
                return;
            }

            // Register user
            User user = userService.registerUser(username, password);

            if (user != null) {
                jsonResponse.put("success", true);
                jsonResponse.put("message", "Registration successful");
                jsonResponse.put("userId", user.getId());
                jsonResponse.put("username", user.getUsername());
            } else {
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Username already exists or registration failed");
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
