package com.example.medireminder.dao;

import com.example.medireminder.model.Medicine;
import com.example.medireminder.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Medicine entity
 * Handles all database operations related to medicines
 * Uses PreparedStatement to prevent SQL injection
 */
public class MedicineDao {

    /**
     * Save a new medicine to the database
     * @param medicine Medicine object to save
     * @return true if save was successful, false otherwise
     */
    public boolean saveMedicine(Medicine medicine) {
        String sql = "INSERT INTO medicines (user_id, name, dosage, notes) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, medicine.getUserId());
            stmt.setString(2, medicine.getName());
            stmt.setString(3, medicine.getDosage());
            stmt.setString(4, medicine.getNotes());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error saving medicine: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Find all medicines for a specific user
     * @param userId User ID
     * @return List of Medicine objects
     */
    public List<Medicine> findByUserId(int userId) {
        List<Medicine> medicines = new ArrayList<>();
        String sql = "SELECT id, user_id, name, dosage, notes FROM medicines WHERE user_id = ? ORDER BY name";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Medicine medicine = new Medicine();
                medicine.setId(rs.getInt("id"));
                medicine.setUserId(rs.getInt("user_id"));
                medicine.setName(rs.getString("name"));
                medicine.setDosage(rs.getString("dosage"));
                medicine.setNotes(rs.getString("notes"));
                medicines.add(medicine);
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding medicines: " + e.getMessage());
            e.printStackTrace();
        }
        
        return medicines;
    }

    /**
     * Delete a medicine by ID
     * @param medicineId Medicine ID to delete
     * @return true if deletion was successful, false otherwise
     */
    public boolean deleteById(int medicineId) {
        String sql = "DELETE FROM medicines WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, medicineId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting medicine: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Find a medicine by ID
     * @param medicineId Medicine ID
     * @return Medicine object if found, null otherwise
     */
    public Medicine findById(int medicineId) {
        String sql = "SELECT id, user_id, name, dosage, notes FROM medicines WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, medicineId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Medicine medicine = new Medicine();
                medicine.setId(rs.getInt("id"));
                medicine.setUserId(rs.getInt("user_id"));
                medicine.setName(rs.getString("name"));
                medicine.setDosage(rs.getString("dosage"));
                medicine.setNotes(rs.getString("notes"));
                return medicine;
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding medicine by ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
}
