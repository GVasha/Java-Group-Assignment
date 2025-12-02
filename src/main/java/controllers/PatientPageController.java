package controllers;

import appointments.Appointment;
import core.ScreenManager;
import core.AppState;
import database_management.DoctorService;
import database_management.PatientService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import users.Doctor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PatientPageController extends BaseController {

    @FXML private Button bookButton;
    @FXML private Button appointmentsButton;
    @FXML private Button profileButton;

    @FXML private TableView<Appointment> appointmentsTable;
    @FXML private TableColumn<Appointment, String> dateColumn;
    @FXML private TableColumn<Appointment, String> timeColumn;
    @FXML private TableColumn<Appointment, String> doctorIdColumn;
    @FXML private TableColumn<Appointment, String> doctorColumn;
    @FXML private TableColumn<Appointment, String> notesColumn;
    @FXML private TableColumn<Appointment, String> statusColumn;

    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private TextField doctorIdField;

    private final AppState appState = AppState.getInstance();

    private final DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("HH:mm");

    @FXML private Label patientGreetingLabel;

    public void initialize() {
        System.out.println("PatientPageController initialized for user ID: " + appState.getUserId());
        patientGreetingLabel.setText("Hello, " + appState.getUser().getName());
        setupTableColumns();
        loadAppointments();
    }

    @FXML
    private void handleBookButton() {
        System.out.println("Book button clicked!");
        // TODO: navigate to booking page
    }

    @FXML
    private void handleAppointmentsButton() {
        System.out.println("Appointments button clicked!");
        // TODO: navigate to appointments page
    }

    @FXML
    private void handleProfileButton() {
        System.out.println("Profile button clicked!");
        // TODO: navigate to patient profile/settings page
    }


    private void setupTableColumns() {

        // Year - Month - Day
        dateColumn.setCellValueFactory(cell -> {
            Appointment appt = cell.getValue();
            LocalDateTime dt = appt.getDate();
            String dateStr = dt != null ? dt.format(dateFmt) : "";
            return new SimpleStringProperty(dateStr);
        });

        // Hour
        timeColumn.setCellValueFactory(cell -> {
            Appointment appt = cell.getValue();
            LocalDateTime dt = appt.getDate();
            String timeStr = dt != null ? dt.format(timeFmt) : "";
            return new SimpleStringProperty(timeStr);
        });

        doctorIdColumn.setCellValueFactory(cell -> {
            Appointment appt = cell.getValue();
            Doctor doctor = appt.getDoctor();
            if (doctor == null) {
                return new SimpleStringProperty(" ");
            } else {
                return new SimpleStringProperty("" + doctor.getId());
            }
        });

        doctorColumn.setCellValueFactory(cell -> {
            Appointment appt = cell.getValue();
            Doctor doctor = appt.getDoctor();
            if (doctor == null) {
                return new SimpleStringProperty("(None)");
            } else {
                return new SimpleStringProperty(doctor.getName());
            }
        });

        notesColumn.setCellValueFactory(cell -> {
            String notes = cell.getValue().getNotes();
            return new SimpleStringProperty(notes != null ? notes : "");
        });

        statusColumn.setCellValueFactory(cell -> {
            String status = cell.getValue().getStatus();
            return new SimpleStringProperty(status != null ? status : "");
        });
    }

    private void loadAppointments() {
        try {
            int patientId = appState.getUserId();
            List<Appointment> appointments = PatientService.fetchAppointmentsForPatientFiltered(
                    patientId, null, null, null
            );
            appointmentsTable.setItems(FXCollections.observableArrayList(appointments));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleFilter() {
        try {
            LocalDateTime start = null;
            LocalDateTime end = null;
            Integer doctorId = null;

            if (startDatePicker != null && startDatePicker.getValue() != null) {
                start = startDatePicker.getValue().atStartOfDay();
            }
            if (endDatePicker != null && endDatePicker.getValue() != null) {
                end = endDatePicker.getValue().atTime(23, 59, 59);
            }
            if (doctorIdField != null && !doctorIdField.getText().trim().isEmpty()) {
              doctorId = Integer.parseInt(doctorIdField.getText().trim());
            }

            int patientId = appState.getUserId();
            List<Appointment> filtered = PatientService.fetchAppointmentsForPatientFiltered(
                    patientId, start, end, doctorId
            );

            appointmentsTable.setItems(FXCollections.observableArrayList(filtered));

        } catch (NumberFormatException nfe) {
            System.out.println("Invalid doctor ID entered.");
            Alert a = new Alert(Alert.AlertType.WARNING, "Invalid doctor ID. Please enter a number.", ButtonType.OK);
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
        if (doctorIdField != null) doctorIdField.clear();
    }
}
