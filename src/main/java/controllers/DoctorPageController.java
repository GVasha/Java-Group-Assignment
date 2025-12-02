package controllers;

import appointments.Appointment;
import core.AppState;
import database_management.DoctorService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DoctorPageController extends BaseController {

    @FXML private Button bookButton;
    @FXML private Button appointmentsButton;
    @FXML private Button profileButton;

    // Use concrete generic types so Java can infer correctly
    @FXML private TableView<Appointment> appointmentsTable;
    @FXML private TableColumn<Appointment, String> dateColumn;
    @FXML private TableColumn<Appointment, String> timeColumn;
    @FXML private TableColumn<Appointment, String> patientColumn;
    @FXML private TableColumn<Appointment, String> reasonColumn;
    @FXML private TableColumn<Appointment, String> statusColumn;

    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private TextField patientIdField;

    private final AppState appState = AppState.getInstance();

    private final DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("HH:mm");

    @FXML private Label doctorGreetingLabel;


    @FXML
    public void initialize() {
        System.out.println("DoctorPageController initialized for user ID: " + appState.getUserId());
        doctorGreetingLabel.setText("Hello, Dr. " + appState.getUser().getLastName());
        setupTableColumns();
        loadAppointments();
    }

    private void setupTableColumns() {
        // Date column -> formatted date part
        dateColumn.setCellValueFactory(cell -> {
            Appointment appt = cell.getValue();
            LocalDateTime dt = appt.getDate();
            String dateStr = dt != null ? dt.format(dateFmt) : "";
            return new SimpleStringProperty(dateStr);
        });

        // Time column -> formatted time part
        timeColumn.setCellValueFactory(cell -> {
            Appointment appt = cell.getValue();
            LocalDateTime dt = appt.getDate();
            String timeStr = dt != null ? dt.format(timeFmt) : "";
            return new SimpleStringProperty(timeStr);
        });

        // Patient column -> patient name or (None)
        patientColumn.setCellValueFactory(cell -> {
            Appointment appt = cell.getValue();
            if (appt.getPatient() == null) {
                return new SimpleStringProperty("(None)");
            } else {
                String fullName = appt.getPatient().getName() + " " + appt.getPatient().getLastName();
                return new SimpleStringProperty(fullName);
            }
        });

        // Reason column -> use notes (or empty)
        reasonColumn.setCellValueFactory(cell -> {
            String notes = cell.getValue().getNotes();
            return new SimpleStringProperty(notes != null ? notes : "");
        });

        // Status column
        statusColumn.setCellValueFactory(cell -> {
            String status = cell.getValue().getStatus();
            return new SimpleStringProperty(status != null ? status : "");
        });

        // Optional: make columns sortable (TableColumn<String, ...> are sortable by default)
    }

    private void loadAppointments() {
        try {
            int doctorId = appState.getUserId();
            List<Appointment> appointments = DoctorService.fetchAppointmentsForDoctorFiltered(
                    doctorId, null, null, null
            );
            appointmentsTable.setItems(FXCollections.observableArrayList(appointments));
        } catch (Exception e) {
            e.printStackTrace();
            // You may want to show a user-facing error message instead of printing
        }
    }

    @FXML
    private void handleBookButton() {
        System.out.println("Doctor clicked Book");
        screenManager.show("doctorBookingPage.fxml");
    }

    @FXML
    private void handleAppointmentsButton() {
        System.out.println("Doctor clicked My Appointments");
        screenManager.show("doctorAppointmentsPage.fxml");
    }

    @FXML
    private void handleProfileButton() {
        System.out.println("Doctor clicked Profile");
        screenManager.show("doctorProfilePage.fxml");
    }

    @FXML
    private void handleFilter() {
        try {
            LocalDateTime start = null;
            LocalDateTime end = null;
            Integer patientId = null;

            if (startDatePicker != null && startDatePicker.getValue() != null) {
                start = startDatePicker.getValue().atStartOfDay();
            }
            if (endDatePicker != null && endDatePicker.getValue() != null) {
                end = endDatePicker.getValue().atTime(23, 59, 59);
            }
            if (patientIdField != null && !patientIdField.getText().trim().isEmpty()) {
                patientId = Integer.parseInt(patientIdField.getText().trim());
            }

            int doctorId = appState.getUserId();
            List<Appointment> filtered = DoctorService.fetchAppointmentsForDoctorFiltered(
                    doctorId, start, end, patientId
            );

            appointmentsTable.setItems(FXCollections.observableArrayList(filtered));

        } catch (NumberFormatException nfe) {
            System.out.println("Invalid patient ID entered.");
            Alert a = new Alert(Alert.AlertType.WARNING, "Invalid patient ID. Please enter a number.", ButtonType.OK);
            a.show();
        } catch (Exception e) {
            e.printStackTrace();
            Alert a = new Alert(Alert.AlertType.ERROR, "Failed to fetch appointments.", ButtonType.OK);
            a.show();
        }
    }

    @FXML
    private void handleReset() {
        loadAppointments();

        if (startDatePicker != null) startDatePicker.setValue(null);
        if (endDatePicker != null) endDatePicker.setValue(null);
        if (patientIdField != null) patientIdField.clear();
    }
}

