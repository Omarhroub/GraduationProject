package com.example.prototype;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class LoginPageController {
    @FXML
    private ImageView loginLogo;
    @FXML
    public TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private Label messageLabel;
    private DatabaseHandler dbHandler;
    private Doctor test;
    @FXML
    private StackPane loginPageStackPane;
    @FXML
    private Button loginButton;



    public void initialize() {
        loginPageStackPane.heightProperty().addListener((obs, oldHeight, newHeight) -> {
            double height = newHeight.doubleValue();
            System.out.println("Height of mainAnchorPane: " + height);
        });

        loginPageStackPane.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            double width = newWidth.doubleValue();
            System.out.println("Width of mainAnchorPane: " + width);
        });


        Platform.runLater(() -> loginButton.requestFocus());
        loginLogo.setImage(new Image("C:\\Users\\Omar\\IdeaProjects\\Prototype\\src\\main\\java\\com\\example\\prototype\\Images\\LoginLogo.png"));
        dbHandler = new DatabaseHandler();

        Image image = new Image("C:\\Users\\Omar\\IdeaProjects\\Prototype\\src\\main\\java\\com\\example\\prototype\\Images\\LoginCliniDesk.png");
        BackgroundImage backgroundImage = new BackgroundImage(
                image,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT);
        Background background = new Background(backgroundImage);
        loginPageStackPane.setBackground(background);

    }

    public void handleLoginButton() {
        String username = usernameField.getText();
        String password = passwordField.getText();


        if (dbHandler.verifyCredentials(username,password)) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("MainPage.fxml"));
                Parent root = loader.load();

                MainPageController controller = loader.getController();
                controller.setActiveDoctor(getTest());
                Scene scene = new Scene(root);
                Stage stage = (Stage) usernameField.getScene().getWindow();
                stage.setScene(scene);
                stage.setResizable(false);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            messageLabel.setText("*Invalid username or password");
        }
    }

    public void quickLogin() {
        usernameField.setText("Omar Hroub");
        passwordField.setText("omar1324");
    }


    public Doctor getTest() {
        test = dbHandler.getDoctorByFullName(usernameField.getText());
        return test;
    }

    public void handleRegister() {
        try {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("RegisterPage.fxml"));
            Parent root = fxmlLoader.load();


            Stage stage = new Stage();
            stage.setTitle("Register New Doctor");
            stage.setScene(new Scene(root));
            File iconFile = new File("C:\\Users\\Omar\\IdeaProjects\\Prototype\\src\\main\\java\\com\\example\\prototype\\Images\\LoginLogo1.png"); // Replace "icon.png" with the path to your icon image
            if (iconFile.exists()) {
                FileInputStream iconStream = new FileInputStream(iconFile);
                Image iconImage = new Image(iconStream);
                stage.getIcons().add(iconImage);
            } else {
                System.out.println("Icon file not found.");
            }

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //    private boolean verifyLogin() {
//        if ((usernameField.getText().equals("admin")) && (passwordField.getText().equals("admin"))) {
//            return true;
//        } else return false;
//    }

}
