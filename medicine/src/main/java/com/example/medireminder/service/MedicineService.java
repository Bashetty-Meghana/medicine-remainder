package com.example.medireminder.service;

import com.example.medireminder.dao.MedicineDao;
import com.example.medireminder.model.Medicine;

import java.util.List;

/**
 * Service layer for Medicine operations
 * Contains business logic for medicine management
 */
public class MedicineService {
    private final MedicineDao medicineDao;

    public MedicineService() {
        this.medicineDao = new MedicineDao();
    }

    /**
     * Add a new medicine for a user
     * @param userId User ID
     * @param name Medicine name
     * @param dosage Dosage
     * @param notes Notes
     * @return true if medicine was added successfully, false otherwise
     */
    public boolean addMedicine(int userId, String name, String dosage, String notes) {
        // Validate input
        if (name == null || name.trim().isEmpty()) {
            System.err.println("Medicine name cannot be empty");
            return false;
        }

        // Create medicine object
        Medicine medicine = new Medicine(userId, name, dosage, notes);
        
        // Save to database
        return medicineDao.saveMedicine(medicine);
    }

    /**
     * Get all medicines for a user
     * @param userId User ID
     * @return List of Medicine objects
     */
    public List<Medicine> getUserMedicines(int userId) {
        return medicineDao.findByUserId(userId);
    }

    /**
     * Delete a medicine
     * @param medicineId Medicine ID to delete
     * @return true if deletion was successful, false otherwise
     */
    public boolean deleteMedicine(int medicineId) {
        return medicineDao.deleteById(medicineId);
    }

    /**
     * Get a medicine by ID
     * @param medicineId Medicine ID
     * @return Medicine object if found, null otherwise
     */
    public Medicine getMedicineById(int medicineId) {
        return medicineDao.findById(medicineId);
    }
}
