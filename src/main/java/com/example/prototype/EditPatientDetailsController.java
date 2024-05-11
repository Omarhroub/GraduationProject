package com.example.prototype;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

public class EditPatientDetailsController {
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
    Patient currPatient;
    @FXML
    private AnchorPane editPatientAnchorPane;

    public void initialize(){
        Image image = new Image("C:\\Users\\Omar\\IdeaProjects\\Prototype\\src\\main\\java\\com\\example\\prototype\\Images\\DoctorDashboard.png");
        BackgroundImage backgroundImage = new BackgroundImage(
                image,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT);
        Background background = new Background(backgroundImage);
        editPatientAnchorPane.setBackground(background);
    }

    public void setFields(Patient patient) {
        currPatient = patient;
        fullNameField.setText(currPatient.getFullName());
        ageField.setText(Integer.toString(currPatient.getAge()));
        genderField.setText(currPatient.getGender());
        addressField.setText(currPatient.getAddress());
        phoneNumberField.setText(currPatient.getPhoneNumber());
        dobField.setValue(currPatient.getDateOfBirth());
    }

    public Patient  getUpdatedPatient(){
        currPatient.setFullName(fullNameField.getText());
        currPatient.setAge(Integer.parseInt(ageField.getText()));
        currPatient.setGender(genderField.getText());
        currPatient.setAddress(addressField.getText());
        currPatient.setPhoneNumber(phoneNumberField.getText());
        currPatient.setDateOfBirth(dobField.getValue());
        return currPatient;
    }
}
