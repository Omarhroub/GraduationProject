package com.example.prototype;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Patient {
    private String fullName;
    private int age;
    private int id;
    private String gender;
    private String address;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private LocalDate dateJoined;
    private ObservableList<Appointment> appointmentsList;
    private String docId;
    private Image imagePath = new Image("C:\\Users\\Omar\\IdeaProjects\\GraduationProject\\src\\main\\java\\com\\example\\graduationproject\\Images\\default patient image.jpg");

    public Patient() {

    }
    public Patient(String fullName, int age, String gender, String address, String phoneNumber, LocalDate dateOfBirth,LocalDate dateJoined) {
        this.fullName = fullName;
        this.age = age;
//        this.id = id;
        this.gender = gender;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
        this.dateJoined = dateJoined;
        appointmentsList = FXCollections.observableArrayList();
    }

    public StringProperty getTableName(){
        StringProperty alo = new SimpleStringProperty(fullName);
        return alo;
    }
    public StringProperty getTableId(){
        StringProperty alo = new SimpleStringProperty(Integer.toString(id));
        return alo;
    }


    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public LocalDate getDateJoined() {
        return dateJoined;
    }

    public void setDateJoined(LocalDate dateJoined) {
        this.dateJoined = dateJoined;
    }

    public List<Appointment> getAppointmentsList() {
        return appointmentsList;
    }

    public void setAppointmentsList(ObservableList<Appointment> appointmentsList) {
        this.appointmentsList = appointmentsList;
    }

    public void addAppointment(Appointment appointment) {
        appointmentsList.add(appointment);
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public void removeAppointment(Appointment appointment) {
        appointmentsList.remove(appointment);
    }

    public Image getImagePath() {
        return imagePath;
    }

    public void setImagePath(Image imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public String toString() {
        if (gender.equals("male")) {
            return (this.fullName);
        }
        else return (this.fullName);
    }
}