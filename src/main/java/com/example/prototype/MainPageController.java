package com.example.prototype;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainPageController implements Initializable {

    //    private Doctor activeDoctor = new Doctor("Omar Hroub","1","omar1324","079");
    Doctor activeDoctor;
    @FXML
    private Label todaysDate;
    @FXML
    private TableView<Patient> patientsTableView;
    @FXML
    private TableColumn<Patient, String> patientsName;
    @FXML
    private TableColumn<Patient, String> patientsId;
    @FXML
    private TableColumn<Patient, Void> editColumn; // Change the type to Void for button cells
    @FXML
    private TableColumn<Patient, Void> appointmentColumn;
    @FXML
    private Circle doctorPicture;
    DatabaseHandler db = new DatabaseHandler();
    @FXML
    private Label doctorName;
    @FXML
    private BorderPane mainBorderPain;
    @FXML
    private TextField searchTextField;
    @FXML
    private AnchorPane mainAnchorPane;
    @FXML
    private TableView<Appointment> appointmentsTableView;
    @FXML
    private TableColumn<Appointment, String> appointmentIdColumn;
    @FXML
    private TableColumn<Appointment, String> appointmentDateColumn;
    @FXML
    private TableColumn<Appointment, String> patientsNameColumn;
    @FXML
    private TableColumn<Appointment, String> ShortDescriptionColumn;
    @FXML
    private TableColumn<Appointment, Void> detailsColumn;
    @FXML
    private Button appointmentsButton;
    @FXML
    private Button patientsButton;
    @FXML
    private TextField searchTextField1;

    public void refreshPatientsData() {
        patientsButton.setDisable(true);
        appointmentsButton.setDisable(false);
        initiatePatients();
        initiateAppointments();
        patientsTableView.setItems(activeDoctor.getPatientsList());
        setPatientsCellColumn();
    }

    public void refreshAppointmentsData() {
        patientsButton.setDisable(false);
        appointmentsButton.setDisable(true);
        appointmentsTableView.setItems(db.getAllAppointments(activeDoctor.getId()));
        setAppointmentsCellColumn();
    }


    public void handleInitialization() {
        Image image = new Image("C:\\Users\\Omar\\IdeaProjects\\Prototype\\src\\main\\java\\com\\example\\prototype\\Images\\Testtt.png");
        BackgroundImage backgroundImage = new BackgroundImage(
                image,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT);
        Background background = new Background(backgroundImage);
        mainBorderPain.setBackground(background);

        doctorPicture.setFill(new ImagePattern(activeDoctor.getImagePath()));
        doctorName.setText(activeDoctor.getFullName());
        todaysDate.setText(LocalDate.now().toString());

        fillPatientsInTable();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mainAnchorPane.heightProperty().addListener((obs, oldHeight, newHeight) -> {
            double height = newHeight.doubleValue();
            System.out.println("Height of mainAnchorPane: " + height);
        });

        mainAnchorPane.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            double width = newWidth.doubleValue();
            System.out.println("Width of mainAnchorPane: " + width);
        });
    }

    @FXML
    public void fillPatientsInTable() {
        patientsTableView.setVisible(true);
        appointmentsTableView.setVisible(false);
        searchTextField.setVisible(true);
        searchTextField1.setVisible(false);

        refreshPatientsData();
    }

    @FXML
    public void fillAppointmentsInTable() {
        patientsTableView.setVisible(false);
        appointmentsTableView.setVisible(true);
        searchTextField.setVisible(false);
        searchTextField1.setVisible(true);

        refreshAppointmentsData();
    }

    public void setActiveDoctor(Doctor activeDoctor) {
        this.activeDoctor = activeDoctor;
        activeDoctor.setPatientsList(FXCollections.observableArrayList());
        handleInitialization();
    }

    private void initiatePatients() {
        activeDoctor.setPatientsList(db.getDoctorsPatients(activeDoctor.getId()));
    }

    private void initiateAppointments() {
        for (Patient patient : activeDoctor.getPatientsList()) {
            patient.setAppointmentsList(db.getPatientAppointments(patient.getId()));
        }
    }

    private void setPatientsCellColumn() {

        patientsName.setCellValueFactory(cellData -> cellData.getValue().getTableName());
        patientsId.setCellValueFactory(cellData -> cellData.getValue().getTableId());
        appointmentColumn.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Patient, Void> call(TableColumn<Patient, Void> param) {
                return new TableCell<>() {
                    private final Button appointmentButton = new Button(" Appointments ");

                    {
                        appointmentButton.setOnAction(event -> {
                            Patient patient = getTableView().getItems().get(getIndex());
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("AppointmentPage.fxml"));
                            try {
                                Parent root = loader.load();
                                AppointmentPageController controller = loader.getController();
                                controller.initData(patient, activeDoctor.getId());
                                Stage stage = new Stage();
                                stage.setScene(new Scene(root));
                                stage.setResizable(false);
                                stage.show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(appointmentButton);
                        }
                    }
                };
            }
        });

        editColumn.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Patient, Void> call(TableColumn<Patient, Void> param) {
                return new TableCell<>() {
                    private final Button editButton = new Button("Edit");
                    private final Button deleteButton = new Button("Delete");

                    {
                        editButton.setOnAction(event -> {
                            // Action to perform when the edit button is clicked
                            Patient patient = getTableView().getItems().get(getIndex());
                            System.out.println("Edit button clicked for: " + patient.getFullName());

                            Dialog<ButtonType> dialog = new Dialog<>();
                            dialog.initOwner(mainBorderPain.getScene().getWindow());
                            dialog.setTitle("Edit Patient");
                            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("EditPatientDetails.fxml"));
                            try {
                                dialog.getDialogPane().setContent(fxmlLoader.load());
                            } catch (IOException e) {
                                e.printStackTrace();
                                System.out.println("Couldn't load the Edit patient dialouge");
                                return;
                            }

                            dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
                            dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);


                            Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);

                            okButton.addEventFilter(ActionEvent.ACTION, eventt -> {
                                EditPatientDetailsController controller = fxmlLoader.getController();
                                if (!controller.validateAgeField()) {
                                    eventt.consume();
                                }
                            });

                            EditPatientDetailsController controller = fxmlLoader.getController();
                            controller.setFields(patient);

                            Optional<ButtonType> result = dialog.showAndWait();

                            if (result.isPresent() && result.get() == ButtonType.OK) {
                                Patient updatedPatient = controller.getUpdatedPatient();
                                try {
                                    db.updatePatient(updatedPatient);
                                    initiatePatients();
                                    initiateAppointments();
                                    patientsTableView.getItems().setAll(activeDoctor.getPatientsList());
                                    showAlert(Alert.AlertType.INFORMATION, "Success", "Patient details updated successfully!");
                                } catch (Exception e) {
                                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to update patient details!");
                                    e.printStackTrace(); // Print the stack trace for debugging purposes
                                }
                            }
                        });

                        deleteButton.setOnAction(event -> {
                            Patient patient = getTableView().getItems().get(getIndex());
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("Patient Deletion");
                            alert.setHeaderText("Delete Patient: " + patient.getFullName());
                            alert.setContentText("Are you sure u want to delete the patient , press okay to confirm or cancel to back out");
                            Optional<ButtonType> result = alert.showAndWait();
                            if (result.isPresent() && (result.get() == ButtonType.OK)) {
                                db.deletePatient(Integer.toString(patient.getId()));
                                activeDoctor.removePatient(patient);

                            }
                            System.out.println("Delete button clicked for: " + patient.getFullName());
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            HBox buttonsContainer = new HBox(editButton, deleteButton);
                            buttonsContainer.setSpacing(5);
                            buttonsContainer.setAlignment(Pos.CENTER);
                            setGraphic(buttonsContainer);
                        }
                    }
                };
            }
        });
    }

    private void setAppointmentsCellColumn() {
        appointmentIdColumn.setCellValueFactory(cellData -> cellData.getValue().getAppointmentTableId());
        appointmentDateColumn.setCellValueFactory(cellData -> cellData.getValue().getAppointmentTableDate());
        patientsNameColumn.setCellValueFactory(cellData -> cellData.getValue().getAppointmentTablePatientsName());
        ShortDescriptionColumn.setCellValueFactory(cellData -> cellData.getValue().getAppointmentTableShortDescription());
        detailsColumn.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Appointment, Void> call(TableColumn<Appointment, Void> param) {
                return new TableCell<>() {
                    private final Button detailsButton = new Button("Details");

                    {
                        detailsButton.setOnAction(event -> {
                            Appointment appointment = getTableView().getItems().get(getIndex());
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("AppointmentPage.fxml"));
                            try {
                                Patient p = db.getPatient(appointment.getPatientId());
                                p.setAppointmentsList(db.getPatientAppointments(p.getId()));
                                Parent root = loader.load();
                                AppointmentPageController controller = loader.getController();
                                controller.initData2(p, activeDoctor.getId(), appointment);
                                Stage stage = new Stage();
                                stage.setScene(new Scene(root));
                                stage.setResizable(false);
                                stage.show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(detailsButton);
                        }
                    }
                };
            }
        });
    }

    @FXML
    public void handleNewPatientDialouge() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPain.getScene().getWindow());
        dialog.setTitle("Add a new Patient");

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AddNewPatientDialouge.fxml"));
        try {
            Image image = new Image("C:\\Users\\Omar\\IdeaProjects\\Prototype\\src\\main\\java\\com\\example\\prototype\\Images\\AppointmentsPage.png");
            BackgroundImage backgroundImage = new BackgroundImage(
                    image,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    BackgroundSize.DEFAULT);
            Background background = new Background(backgroundImage);
            dialog.getDialogPane().setBackground(background);

            dialog.getDialogPane().setContent(fxmlLoader.load());

        } catch (IOException e) {
            System.out.println("Couldnt load the new patient dialouge");
            e.printStackTrace();
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

// Get the OK button so you can handle its action
        Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);

        okButton.addEventFilter(ActionEvent.ACTION, event -> {
            AddNewPatientController controller = fxmlLoader.getController();
            if (!controller.validateAgeField()) {
                event.consume(); // Prevent dialog from closing if validation fails
            }
        });

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            AddNewPatientController controller = fxmlLoader.getController();

            // Since we validated before, no need to validate again
            Patient newPatient = controller.processResult();
            newPatient.setDocId(activeDoctor.getId());
            try {
                String patientId = db.addNewPatient(newPatient);
                if (patientId != null && !patientId.isEmpty()) {
                    newPatient.setId(Integer.parseInt(patientId));
                    activeDoctor.addPatient(newPatient);
                    patientsTableView.setItems(activeDoctor.getPatientsList());
                    patientsTableView.getSelectionModel().select(newPatient);
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Patient added successfully!");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to add patient!");
                }
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to add patient!");
                e.printStackTrace(); // Print the stack trace for debugging purposes
            }
        }

    }

    public void handleSearchButton() {
        patientsTableView.getItems().clear();
        patientsTableView.getItems().addAll(returnFilteredList(searchTextField.getText()));
    }

    public ObservableList<Patient> returnFilteredList(String searchString) {
        initiatePatients();
        initiateAppointments();
        ObservableList<Patient> searchList = FXCollections.observableArrayList();
        for (Patient patient : activeDoctor.getPatientsList()) {
            if (patient.getFullName().toLowerCase().contains(searchString.toLowerCase())) {
                searchList.add(patient);
            }
        }
        return searchList;
    }

    public void handleAppointmentSearchButton() {
        appointmentsTableView.getItems().clear();
        appointmentsTableView.getItems().addAll(returnAppointmentFilteredList(searchTextField1.getText()));
    }

    public ObservableList<Appointment> returnAppointmentFilteredList(String searchString) {
        initiatePatients();
        initiateAppointments();
        ObservableList<Appointment> searchList = FXCollections.observableArrayList();
        for (Appointment appointment : db.getAllAppointments(activeDoctor.getId())) {
            if (appointment.getShortDescription().toLowerCase().contains(searchString.toLowerCase())) {
                searchList.add(appointment);
            }
            String appointmentDate = appointment.getDate().toString();
            if (appointmentDate.contains(searchString)){
                if(!searchList.contains(appointment)){
                    searchList.add(appointment);
                }
            }
        }

        return searchList;
    }

    @FXML
    private void handleClickPfpPIC() {
        ContextMenu pfpContextMenu = new ContextMenu();
        MenuItem changePfp = new MenuItem("Change Profile Picture...");

        // Add action to the menu item
        changePfp.setOnAction(event -> {
            // Your code to handle the action
            System.out.println("Change Profile Picture clicked");
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Image File");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif", "*.bmp"));
            File selectedFile = fileChooser.showOpenDialog(mainBorderPain.getScene().getWindow());
            if (selectedFile != null) {
                Image image = new Image(selectedFile.toURI().toString());
                activeDoctor.setImagePath(image);
                doctorPicture.setFill(new ImagePattern(activeDoctor.getImagePath()));


            }
        });

        // Add menu item to the context menu
        pfpContextMenu.getItems().add(changePfp);

        // Add event handler to the Circle to show the ContextMenu on right-click
        doctorPicture.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                pfpContextMenu.show(doctorPicture, event.getScreenX(), event.getScreenY());
            } else {
                pfpContextMenu.hide();
            }
        });
    }


    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}