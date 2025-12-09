package com.example.medireminder.model;

/**
 * Medicine POJO (Plain Old Java Object)
 * Represents a medicine entry for a user
 */
public class Medicine {
    private int id;
    private int userId;
    private String name;
    private String dosage;
    private String notes;

    // Constructors
    public Medicine() {
    }

    public Medicine(int userId, String name, String dosage, String notes) {
        this.userId = userId;
        this.name = name;
        this.dosage = dosage;
        this.notes = notes;
    }

    public Medicine(int id, int userId, String name, String dosage, String notes) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.dosage = dosage;
        this.notes = notes;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "Medicine{" +
                "id=" + id +
                ", userId=" + userId +
                ", name='" + name + '\'' +
                ", dosage='" + dosage + '\'' +
                ", notes='" + notes + '\'' +
                '}';
    }
}
