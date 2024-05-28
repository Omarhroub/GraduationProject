package com.example.prototype;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AddNewAppointmentController {
    @FXML
    private TextField shortDescription;
    @FXML
    private DatePicker appointmentDate;
    @FXML
    private ComboBox<Integer> hoursComboBox;
    @FXML
    private ComboBox<Integer> minutesComboBox;
    DatabaseHandler db = new DatabaseHandler();
    @FXML
    private AnchorPane addNewAppointmentAnchorPane;

    public void initialize() {
                Image image = new Image("C:\\Users\\Omar\\IdeaProjects\\Prototype\\src\\main\\java\\com\\example\\prototype\\Images\\AppointmentsPage.png");
        BackgroundImage backgroundImage = new BackgroundImage(
                image,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT);
        Background background = new Background(backgroundImage);
        addNewAppointmentAnchorPane.setBackground(background);

        hoursComboBox.getItems().add(8);
        hoursComboBox.getItems().add(9);
        hoursComboBox.getItems().add(10);
        hoursComboBox.getItems().add(11);
        hoursComboBox.getItems().add(12);
        hoursComboBox.getItems().add(13);
        hoursComboBox.getItems().add(14);
        hoursComboBox.getItems().add(15);
        hoursComboBox.getItems().add(16);
        hoursComboBox.getItems().add(17);

        for (int i = 0; i < 60; i++) {
            minutesComboBox.getItems().add(i);
        }
    }

    public Appointment processData() {
        Appointment appointment = new Appointment();
        appointment.setShortDescription(shortDescription.getText());
        appointment.setDate(appointmentDate.getValue());
        appointment.setSelectedHour(hoursComboBox.getValue());
        appointment.setSelectedMinute(minutesComboBox.getValue());
        appointment.setNotes("");
        appointment.setDiagnosis("");
        appointment.setPrescription("");
        appointment.setStatus("Scheduled");
        appointment.setPaymentStatus(false);
        return appointment;
    }

    @FXML
    public void handleGenerateDateAndTime() {
        LocalDate tmrwsDate = LocalDate.now().plusDays(1);


        boolean breakOuterLoop = false;
        for (int i = 1;i <= 5;i++) {
            List<Integer> appointmentHours = db.getAppointmentHours(tmrwsDate);

            for (int hour = 8; hour <= 17; hour++) {
                if(!appointmentHours.contains(hour)){
                    System.out.println(hour);
                    hoursComboBox.setValue(hour);
                    minutesComboBox.setValue(0);
                    appointmentDate.setValue(tmrwsDate);
                    breakOuterLoop = true;
                    break;
                }
            }
            if (breakOuterLoop){
                break;
            }
           tmrwsDate = tmrwsDate.plusDays(1);
        }
    }
}
