package controllers;

import appointments.Appointment;
import core.AppState;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import users.Patient;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class PatientBookingPageController extends BaseController {

    @FXML private DatePicker appointmentDatePicker;
    @FXML private ComboBox<String> specializationCombo;

    @FXML private Button bookAppointment;

    @FXML private TableView<Appointment> appointmentsTable;
    @FXML private TableColumn<Appointment, String> dateColumn;
    @FXML private TableColumn<Appointment, String> timeColumn;
    @FXML private TableColumn<Appointment, String> doctorColumn;
    @FXML private TableColumn<Appointment, String> specializationColumn;
    @FXML private TableColumn<Appointment, String> notesColumn;

    private final DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("HH:mm");

    private List<Appointment> allAppointments;
    private AppState appState = AppState.getInstance();
    private Patient patient = (Patient) appState.getUser();

    @FXML
    public void initialize(){
        // Populate specialization combo
        specializationCombo.getItems().addAll(
                "Cardiology", "Dermatology", "Neurology", "Pediatrics", "General Medicine"
        );

        initializeActionButtons();
        setupTableColumns();
        setupSelectionListener();
        loadAvailableAppointments();
    }

    private void initializeActionButtons(){
        if (bookAppointment != null) bookAppointment.setDisable(true);
    }

    private void setButtonsState(boolean selected, Appointment selectedAppointment){
        if (bookAppointment != null){
            bookAppointment.setDisable(!selected);
        }
    }

    private void setupSelectionListener(){
        if (appointmentsTable == null) return;

        appointmentsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            boolean selected = newSel != null;
            setButtonsState(selected, newSel);
        });
    }

    private void setupTableColumns() {
        dateColumn.setCellValueFactory(cell -> new SimpleStringProperty(
                cell.getValue().getDate().format(dateFmt)));
        timeColumn.setCellValueFactory(cell -> new SimpleStringProperty(
                cell.getValue().getDate().format(timeFmt)));
        doctorColumn.setCellValueFactory(cell -> new SimpleStringProperty(
                cell.getValue().getDoctor().getName()));
        specializationColumn.setCellValueFactory(cell -> new SimpleStringProperty(
                cell.getValue().getDoctor().getSpecialization()));
        notesColumn.setCellValueFactory(cell -> new SimpleStringProperty(
                cell.getValue().getNotes() != null ? cell.getValue().getNotes() : ""));
    }

    private void loadAvailableAppointments() {
        try {
            allAppointments = patient.searchAvailableAppointments(null, null, null, null); // Fetch all available
            appointmentsTable.getItems().setAll(allAppointments);
        } catch (Exception e) {
            e.printStackTrace();
            showError("Failed to load available appointments.", e);
        }
    }

    @FXML
    private void handleFilter() {
        List<Appointment> filtered = allAppointments;

        if (appointmentDatePicker.getValue() != null) {
            LocalDate selectedDate = appointmentDatePicker.getValue();
            filtered = filtered.stream()
                    .filter(a -> a.getDate().toLocalDate().equals(selectedDate))
                    .collect(Collectors.toList());
        }

        if (specializationCombo.getValue() != null && !specializationCombo.getValue().isEmpty()) {
            String specialization = specializationCombo.getValue().trim();
            filtered = filtered.stream()
                    .filter(a -> a.getDoctor().getSpecialization().equalsIgnoreCase(specialization))
                    .collect(Collectors.toList());
        }

        appointmentsTable.getItems().setAll(filtered);
    }

    @FXML
    private void handleBookAppointment(){
        Appointment selectedAppointment = appointmentsTable.getSelectionModel().getSelectedItem();
        patient.bookAppointment(selectedAppointment);
        loadAvailableAppointments();
    }

    @FXML
    private void handleReset() {
        appointmentDatePicker.setValue(null);
        specializationCombo.setValue(null);
        appointmentsTable.setItems(FXCollections.observableArrayList(allAppointments));
    }

    @FXML
    private void handleBackButton() {
        screenManager.show("patientLandingPage.fxml");
    }

    private void showError(String message, Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(message);
        alert.setContentText(e.getMessage());
        alert.showAndWait();
    }
}