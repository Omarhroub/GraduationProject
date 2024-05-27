package com.example.prototype;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

public class EditPatientDetailsController {
    @FXML
    private TextField fullNameField;
    @FXML
    private TextField ageField;
    @FXML
    private ComboBox<String> genderComboBox;
    @FXML
    private TextField addressField;
    @FXML
    private TextField phoneNumberField;
    @FXML
    private DatePicker dobField;
    Patient currPatient;
    @FXML
    private AnchorPane editPatientAnchorPane;
    @FXML
    private Label ageErrorLabel;

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
        genderComboBox.getItems().add("Male");
        genderComboBox.getItems().add("Female");

        fullNameField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[a-zA-Z ]*")) {
                fullNameField.setText(newValue.replaceAll("[^a-zA-Z ]", ""));
            }
        });

        ageField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                ageField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        phoneNumberField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[\\d+\\- ]*")) {
                phoneNumberField.setText(newValue.replaceAll("[^\\d+\\- ]", ""));
            }
        });
    }

    public void setFields(Patient patient) {
        currPatient = patient;
        fullNameField.setText(currPatient.getFullName());
        ageField.setText(Integer.toString(currPatient.getAge()));
        genderComboBox.setValue(currPatient.getGender());
        addressField.setText(currPatient.getAddress());
        phoneNumberField.setText(currPatient.getPhoneNumber());
        dobField.setValue(currPatient.getDateOfBirth());
    }

    public Patient  getUpdatedPatient(){
        currPatient.setFullName(fullNameField.getText());
        currPatient.setAge(Integer.parseInt(ageField.getText()));
        currPatient.setGender(genderComboBox.getValue());
        currPatient.setAddress(addressField.getText());
        currPatient.setPhoneNumber(phoneNumberField.getText());
        currPatient.setDateOfBirth(dobField.getValue());
        return currPatient;
    }

    public boolean validateAgeField() {
        String ageText = ageField.getText();
        try {
            int age = Integer.parseInt(ageText);
            if (age < 0) {
                ageErrorLabel.setText("Age cannot be negative!");
                return false;
            } else if (age > 100) {
                ageErrorLabel.setText("Age cannot be more than 100!");
                return false;
            } else {
                ageErrorLabel.setText(""); // Clear the error message
                return true;
            }
        } catch (NumberFormatException e) {
            ageErrorLabel.setText("Age must be a valid number!");
            return false;
        }
    }
}
