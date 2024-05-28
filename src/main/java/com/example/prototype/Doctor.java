package com.example.prototype;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;

public class Doctor {
    private String fullName;
    private String id;
    private ObservableList<Patient> patientsList;
    private String phoneNumber;
    private String password;
    private Image imagePath = new Image("C:\\Users\\Omar\\IdeaProjects\\Prototype\\src\\main\\java\\com\\example\\prototype\\Images\\default_dr.jpg");

    public Doctor() {

    }
    public Doctor(String fullName, String id, String password, String phoneNumber) {
        this.fullName = fullName;
        this.id = id;
        this.password = password;
        this.phoneNumber = phoneNumber;
        patientsList = FXCollections.observableArrayList();
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ObservableList<Patient> getPatientsList() {
        return patientsList;
    }

    public void addPatient(Patient patient) {
        patientsList.add(patient);
    }

    public void removePatient(Patient patient) {
        patientsList.remove(patient);
    }

    public void setPatientsList(ObservableList<Patient> patientsList) {
        this.patientsList = patientsList;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Image getImagePath() {
        return imagePath;
    }

    public void setImagePath(Image imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public String toString() {
        return "to string " + fullName;
    }
}