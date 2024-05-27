package com.example.prototype;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

public class AppointmentReportController {
    @FXML
    private AnchorPane appointmentRecordAnchorPane;
    @FXML
    private Label dateAndTimeLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private Label genderLabel;
    @FXML
    private Label phoneLabel;
    @FXML
    private Label ageLabel;
    @FXML
    private Label diagnosesLabel;
    @FXML
    private Label prescriptionLabel;
    private Patient patient;
    private Appointment appointment;

    public void setPatient(Patient patient) {
        this.patient = patient;
        setFields();
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
        setFields();
    }

    public void initialize() {
        Image image = new Image("C:\\Users\\Omar\\IdeaProjects\\Prototype\\src\\main\\java\\com\\example\\prototype\\Images\\PatientReport.png");
        BackgroundImage backgroundImage = new BackgroundImage(
                image,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT);
        Background background = new Background(backgroundImage);
        appointmentRecordAnchorPane.setBackground(background);
    }

    private void setFields() {
        if (patient != null && appointment != null) {
            dateAndTimeLabel.setText(appointment.getDate() + " || " + formatTime(appointment.getSelectedHour(), appointment.getSelectedMinute()));
            nameLabel.setText(patient.getFullName());
            genderLabel.setText(patient.getGender());
            phoneLabel.setText(patient.getPhoneNumber());
            ageLabel.setText(Integer.toString(patient.getAge()));
            diagnosesLabel.setText(appointment.getDiagnosis());
            prescriptionLabel.setText(appointment.getPrescription());
        }
    }

    private String formatTime(int hour, int minute) {
        String period = hour >= 12 ? "PM" : "AM";
        int formattedHour = hour % 12;
        if (formattedHour == 0) {
            formattedHour = 12;
        }
        return String.format("%02d:%02d %s", formattedHour, minute, period);
    }
}
