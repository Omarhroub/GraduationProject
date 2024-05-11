package com.example.prototype;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
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

    public void refreshData(){
        initiatePatients();
        initiateAppointments();
        patientsTableView.setItems(activeDoctor.getPatientsList());
        setCellColumn();
    }
    public void handleInitialization(){
        Image image = new Image("C:\\Users\\Omar\\IdeaProjects\\Prototype\\src\\main\\java\\com\\example\\prototype\\Images\\DoctorDashboard.png");
        BackgroundImage backgroundImage = new BackgroundImage(
                image,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT);
        Background background = new Background(backgroundImage);
        mainAnchorPane.setBackground(background);


        doctorPicture.setFill(new ImagePattern(activeDoctor.getImagePath()));
        doctorName.setText(activeDoctor.getFullName());
        todaysDate.setText(LocalDate.now().toString());

        refreshData();
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

    private void setCellColumn() {

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
                                controller.initData(patient,activeDoctor.getId());
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
                                System.out.println("Couldn't load the Edit patient dialouge");
                                e.printStackTrace();
                                return;
                            }

                            dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
                            dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
                            EditPatientDetailsController controller = fxmlLoader.getController();
                            controller.setFields(patient);
                            Optional<ButtonType> result = dialog.showAndWait();

                            if (result.isPresent() && result.get() == ButtonType.OK) {
                                System.out.println(controller.getUpdatedPatient());
                                patient = controller.getUpdatedPatient();
                                db.updatePatient(patient);
                                initiatePatients();
                                initiateAppointments();
                                patientsTableView.getItems().setAll(activeDoctor.getPatientsList());

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
                            setGraphic(buttonsContainer);
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
//        dialog.setHeaderText("Use this dialouge to create a new patient");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AddNewPatientDialouge.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());

        } catch (IOException e) {
            System.out.println("Couldnt load the new patient dialouge");
            e.printStackTrace();
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            AddNewPatientController controller = fxmlLoader.getController();
            Patient newPatient = controller.processResult();
            newPatient.setDocId(activeDoctor.getId());
            activeDoctor.addPatient(newPatient);
            patientsTableView.setItems(activeDoctor.getPatientsList());
            patientsTableView.getSelectionModel().select(newPatient);
            newPatient.setId(Integer.parseInt(db.addNewPatient(newPatient)));

        }
    }

    public void handleSearchButton(){
        patientsTableView.getItems().clear();
        patientsTableView.getItems().addAll(returnFilteredList(searchTextField.getText()));
    }

    public ObservableList<Patient> returnFilteredList(String searchString) {
        ObservableList<Patient> searchList = FXCollections.observableArrayList();
        for (Patient patient : activeDoctor.getPatientsList()) {
            if (patient.getFullName().toLowerCase().contains(searchString.toLowerCase())) {
                searchList.add(patient);
            }
        }

        return searchList;
    }
}