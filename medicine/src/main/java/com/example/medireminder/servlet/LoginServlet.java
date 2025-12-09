package com.example.medireminder.servlet;

import com.example.medireminder.model.User;
import com.example.medireminder.service.UserService;
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
 * Servlet for user login
 * Endpoint: POST /login
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
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

            // Login user
            User user = userService.loginUser(username, password);

            if (user != null) {
                // Create session and store user ID
                HttpSession session = request.getSession();
                session.setAttribute("userId", user.getId());
                session.setAttribute("username", user.getUsername());

                jsonResponse.put("success", true);
                jsonResponse.put("message", "Login successful");
                jsonResponse.put("userId", user.getId());
                jsonResponse.put("username", user.getUsername());
            } else {
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Invalid username or password");
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
