package com.example.prototype;


import javafx.scene.image.Image;

import java.time.LocalDate;

public class Appointment {
    private String id;
    private String patientId;
    private String shortDescription;
    private String notes;
    private LocalDate date;
    private int selectedHour;
    private int selectedMinute;
    private String status;
    private boolean paymentStatus;
    private String prescription;
    private String diagnosis;
    private Image xray;

    public Appointment() {

    }

    public Appointment(String shortDescription, LocalDate date, int selectedHour, int selectedMinute) {

        this.shortDescription = shortDescription;
//        this.notes = notes;
        this.date = date;
        this.selectedHour = selectedHour;
        this.selectedMinute = selectedMinute;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getSelectedHour() {
        return selectedHour ;
    }

    public void setSelectedHour(int selectedHour) {
        this.selectedHour = selectedHour;
    }

    public int getSelectedMinute() {
        return selectedMinute;
    }

    public void setSelectedMinute(int selectedMinute) {
        this.selectedMinute = selectedMinute;
    }

    @Override
    public String toString() {
        return this.getShortDescription();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(boolean paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getPrescription() {
        return prescription;
    }

    public void setPrescription(String prescription) {
        this.prescription = prescription;
    }

    public Image getXray() {
        return xray;
    }

    public void setXray(Image xray) {
        this.xray = xray;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }
}

