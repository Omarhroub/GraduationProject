package com.example.prototype;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
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
    private ComboBox<String> genderComboBox;
    @FXML
    private TextField addressField;
    @FXML
    private TextField phoneNumberField;
    @FXML
    private DatePicker dobField;
    @FXML
    private AnchorPane addNewPatientAnchorPane;
    @FXML
    private Label ageErrorLabel;

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

    public Patient processResult(){
        Patient patient = new Patient();
        patient.setFullName(fullNameField.getText());
        patient.setAge(Integer.parseInt(ageField.getText()));
        patient.setGender(genderComboBox.getValue());
        patient.setAddress(addressField.getText());
        patient.setPhoneNumber(phoneNumberField.getText());
        patient.setDateOfBirth(dobField.getValue());
        patient.setDateJoined(LocalDate.now());
        patient.setAppointmentsList(FXCollections.observableArrayList());
        return patient;
    }
}
