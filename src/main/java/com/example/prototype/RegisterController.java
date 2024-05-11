package com.example.prototype;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class RegisterController {

    @FXML
    private TextField fullNameField;
    @FXML
    private TextField phoneNumberField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private BorderPane registerBorderPane;

    public void initialize(){
        Image image = new Image("C:\\Users\\Omar\\IdeaProjects\\Prototype\\src\\main\\java\\com\\example\\prototype\\Images\\RegisterPage.png");
        BackgroundImage backgroundImage = new BackgroundImage(
                image,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT);
        Background background = new Background(backgroundImage);
        registerBorderPane.setBackground(background);

    }
    public void handleRegister() {
        String fullName = fullNameField.getText();
        String phoneNumber = phoneNumberField.getText();
        String password = passwordField.getText();

        if (fullName.isEmpty() || phoneNumber.isEmpty() || password.isEmpty()) {
            showAlert("Error", "All fields are required.");
            return;
        }

        DatabaseHandler db = new DatabaseHandler();
        Doctor doc = new Doctor();
        doc.setFullName(fullName);
        doc.setPhoneNumber(phoneNumber);
        doc.setPassword(password);
        db.addNewDoctor(doc);

        showAlert("Success", "Doctor registered successfully.");
        goBack();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void goBack() {
        Stage stage = (Stage) fullNameField.getScene().getWindow();
        stage.close();
    }
}
