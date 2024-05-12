package com.example.prototype;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.time.LocalDate;

public class AddNewPatientController {
    @FXML
    private TextField fullNameField;
    @FXML
    private TextField ageField;
    @FXML
    private TextField genderField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField phoneNumberField;
    @FXML
    private DatePicker dobField;
    @FXML
    private AnchorPane addNewPatientAnchorPane;

    public void initialize(){
//        Image image = new Image("C:\\Users\\Omar\\IdeaProjects\\Prototype\\src\\main\\java\\com\\example\\prototype\\Images\\AddEditPatients.jpg");
//        BackgroundImage backgroundImage = new BackgroundImage(
//                image,
//                BackgroundRepeat.NO_REPEAT,
//                BackgroundRepeat.NO_REPEAT,
//                BackgroundPosition.CENTER,
//                BackgroundSize.DEFAULT);
//        Background background = new Background(backgroundImage);
//        addNewPatientAnchorPane.setBackground(background);
    }

    public Patient processResult(){
        Patient patient = new Patient();
        patient.setFullName(fullNameField.getText());
        patient.setAge(Integer.parseInt(ageField.getText()));
        patient.setGender(genderField.getText());
        patient.setAddress(addressField.getText());
        patient.setPhoneNumber(phoneNumberField.getText());
        patient.setDateOfBirth(dobField.getValue());
        patient.setDateJoined(LocalDate.now());
        patient.setAppointmentsList(FXCollections.observableArrayList());
        return patient;
    }
}
