package com.example.prototype;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class AppointmentPageController {
    private Patient curPatient;
    private String docID;
    @FXML
    private ListView<Appointment> appointmentsListView;
    @FXML
    private TextField patientsJoinDate;
    @FXML
    private TextField patientsDob;
    @FXML
    private TextField patientsPhone;
    @FXML
    private TextField patientsAddress;
    @FXML
    private TextField patientsAge;
    @FXML
    private TextField patientsGender;
    @FXML
    private TextField patientsName;
    @FXML
    private ImageView patientsImage;
    @FXML
    private TextField prescriptionField;
    @FXML
    private TextField diagnosisField;
    @FXML
    private TextArea notes;
    @FXML
    private RadioButton scheduledRadioButton;
    @FXML
    private RadioButton cancelledRadioButton;
    @FXML
    private RadioButton completedRadioButton;
    @FXML
    private CheckBox paymentStatusCheckBox;
    @FXML
    private Label appointmentDate;
    @FXML
    private Label appointmentTime;
    @FXML
    private ToggleGroup statusToggleGroup;
    DatabaseHandler db = new DatabaseHandler();
    @FXML
    private Button saveButton;
    @FXML
    private AnchorPane appointmentPageAnchorPane;
    @FXML
    private ComboBox<String> filterComboBox;
    @FXML
    private ImageView appointmentImage;
    @FXML
    private Button importImageButton;



    public void initData(Patient patient, String docId) {
        String[] options = {"Scheduled", "Cancelled", "Completed", "All"};
        filterComboBox.getItems().addAll(options);

        curPatient = patient;
        docID = docId;
        appointmentsListView.getItems().setAll(curPatient.getAppointmentsList());
        setPatientsFields(curPatient);

        addAppointmentContextMenu();

        Image image = new Image("C:\\Users\\Omar\\IdeaProjects\\Prototype\\src\\main\\java\\com\\example\\prototype\\Images\\AppointmentsPage.png");
        BackgroundImage backgroundImage = new BackgroundImage(
                image,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT);
        Background background = new Background(backgroundImage);
        appointmentPageAnchorPane.setBackground(background);

    }

    private void setPatientsFields(Patient patient) {
        patientsName.setText(patient.getFullName());
        patientsGender.setText(patient.getGender());
        patientsAge.setText(Integer.toString(patient.getAge()));
        patientsAddress.setText(patient.getAddress());
        patientsPhone.setText(patient.getPhoneNumber());
        patientsDob.setText(patient.getDateOfBirth().toString());
        patientsJoinDate.setText(patient.getDateJoined().toString());
        patientsImage.setImage(patient.getImagePath());

    }

    @FXML
    public void handleClickAppointmentsListView() {
        clearAppointmentDetails();
        Appointment appointment = appointmentsListView.getSelectionModel().getSelectedItem();
        if (appointment.getNotes() != null) {
            notes.setText(appointment.getNotes());
        }
        if (appointment.getPrescription() != null) {
            prescriptionField.setText(appointment.getPrescription());
        }

        diagnosisField.setText(appointment.getDiagnosis());

        String status = appointment.getStatus();
        if (status != null) {
            if (status.equals("Scheduled")) {
                scheduledRadioButton.setSelected(true);
            } else if (status.equals("Cancelled")) {
                cancelledRadioButton.setSelected(true);
            } else {
                completedRadioButton.setSelected(true);
            }
        }
        if (appointment.isPaymentStatus()) {
            paymentStatusCheckBox.setSelected(true);
        }
        appointmentDate.setText(appointment.getDate().toString());
        String hours = Integer.toString(appointment.getSelectedHour());
        String minutes = Integer.toString(appointment.getSelectedMinute());
        if (appointment.getSelectedHour() < 10) {
            hours = "0" + appointment.getSelectedHour();
        }
        if (appointment.getSelectedMinute() < 10) {
            minutes = "0" + appointment.getSelectedMinute();
        }
        appointmentTime.setText(hours + " : " + minutes);
        if (appointment.getXray() == null) {
            appointmentImage.setImage(null);
        } else {
            appointmentImage.setImage(appointmentImage.getImage());
        }


    }

    @FXML
    public void handleAppointmentDetailsSave() {
        Appointment appointment = appointmentsListView.getSelectionModel().getSelectedItem();
        appointment.setNotes(notes.getText());
        appointment.setDiagnosis(diagnosisField.getText());
        appointment.setPrescription(prescriptionField.getText());
        RadioButton selectedStatus = ((RadioButton) statusToggleGroup.getSelectedToggle());
        if (selectedStatus != null) {
            appointment.setStatus(selectedStatus.getText());
        }
        appointment.setPaymentStatus(paymentStatusCheckBox.isSelected());
        db.updateAppointment(appointment);
    }

    @FXML
    public void handleNewAppointmentDialouge() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(appointmentPageAnchorPane.getScene().getWindow());
        dialog.setTitle("Add a new Appointment");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AddNewAppointmentDialouge.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
            AddNewAppointmentController controller = fxmlLoader.getController();

            dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                Appointment newAppointment = controller.processData();

                if (!db.checkIfAppointmentTimeExists(newAppointment.getDate(),newAppointment.getSelectedHour(),newAppointment.getSelectedMinute())) {
                    curPatient.addAppointment(newAppointment);
                    newAppointment.setPatientId(Integer.toString(curPatient.getId()));
                    newAppointment.setId(db.addNewAppointment(newAppointment, Integer.toString(curPatient.getId()), docID));
                    appointmentsListView.getItems().setAll(curPatient.getAppointmentsList());
                    appointmentsListView.getSelectionModel().select(newAppointment);
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Overlapping appointments");
                    alert.setContentText("The appointment Date and Time already exists.");
                    alert.showAndWait();
                }

            }
        } catch (IOException e) {
            System.out.println("Couldn't load the new appointment dialogue");
            e.printStackTrace();
        }
    }

    public void deleteAppointment(Appointment appointment) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Appointment Deletion");
        alert.setHeaderText("Delete Appointment: " + appointment.getShortDescription());
        alert.setContentText("Are you sure u want to delete the Appointment , press okay to confirm or cancel to back out");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && (result.get() == ButtonType.OK)) {
            db.deleteAppointment(appointmentsListView.getSelectionModel().getSelectedItem().getId());
            curPatient.removeAppointment(appointmentsListView.getSelectionModel().getSelectedItem());
            appointmentsListView.getItems().setAll(curPatient.getAppointmentsList());
        }
    }

    @FXML
    public void handleFilterComboBoxClick() {
        String selectedItem = filterComboBox.getSelectionModel().getSelectedItem();
        if (selectedItem.equals("Scheduled")) {

            ObservableList<Appointment> filteredAppointmentList = db.getFilteredList("Scheduled", Integer.toString(curPatient.getId()), curPatient.getDocId());
            appointmentsListView.getItems().setAll(filteredAppointmentList);

        } else if (selectedItem.equals("Cancelled")) {

            ObservableList<Appointment> filteredAppointmentList = db.getFilteredList("Cancelled", Integer.toString(curPatient.getId()), curPatient.getDocId());
            appointmentsListView.getItems().setAll(filteredAppointmentList);

        } else if (selectedItem.equals("Completed")) {

            ObservableList<Appointment> filteredAppointmentList = db.getFilteredList("Compeleted", Integer.toString(curPatient.getId()), curPatient.getDocId());
            appointmentsListView.getItems().setAll(filteredAppointmentList);

        } else {
            appointmentsListView.getItems().setAll(curPatient.getAppointmentsList());
        }
    }

    private void clearAppointmentDetails() {
        diagnosisField.clear();
        prescriptionField.clear();
        notes.clear();
    }

    private void addAppointmentContextMenu() {
        ContextMenu appointmentListContextMenu = new ContextMenu();
        MenuItem delAppointment = new MenuItem("Delete Appointment..");
        delAppointment.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Appointment appointment = appointmentsListView.getSelectionModel().getSelectedItem();
                deleteAppointment(appointment);
            }
        });
        appointmentListContextMenu.getItems().addAll(delAppointment);
        appointmentsListView.setContextMenu(appointmentListContextMenu);
    }

    @FXML
    private void handleImageImportButton() {
//        importImageButton.setDisable(false);
        Appointment appointment = appointmentsListView.getSelectionModel().getSelectedItem();
        if (appointment == null) {
//            importImageButton.setDisable(true);
        } else {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Image File");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif", "*.bmp"));
            File selectedFile = fileChooser.showOpenDialog(appointmentPageAnchorPane.getScene().getWindow());
            if (selectedFile != null) {
                Image image = new Image(selectedFile.toURI().toString());
                appointmentImage.setImage(image);
                appointment.setXray(image);
                appointmentsListView.getItems().setAll(curPatient.getAppointmentsList());
            }
        }
    }


}


