package controllers;

import appointments.Appointment;
import core.AppState;
import database_management.DoctorService;
import database_management.AppointmentService;
import database_management.UserService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

public class DoctorPageController extends BaseController {

    // Top nav buttons (optional; can be null if not in FXML)
    @FXML private MenuButton profileMenuButton;

    // Bottom action buttons
    @FXML private Button cancelAppointmentButton;
    @FXML private Button deleteAppointmentButton;
    @FXML private Button makeAvailableButton;
    @FXML private Button createAppointmentButton; // if you expose it in FXML

    // Table + columns
    @FXML private TableView<Appointment> appointmentsTable;
    @FXML private TableColumn<Appointment, String> dateColumn;
    @FXML private TableColumn<Appointment, String> timeColumn;
    @FXML private TableColumn<Appointment, String> patientColumn;
    @FXML private TableColumn<Appointment, String> reasonColumn;
    @FXML private TableColumn<Appointment, String> statusColumn;

    // Filters
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private TextField patientIdField;

    @FXML private Label doctorGreetingLabel;

    private final AppState appState = AppState.getInstance();
    private final DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("HH:mm");

    @FXML
    public void initialize() {
        System.out.println("DoctorPageController initialize(), userId = " + appState.getUserId());

        // Be safe if user is not set yet
        if (doctorGreetingLabel != null) {
            if (appState.getUser() != null) {
                doctorGreetingLabel.setText("Hello, Dr. " + appState.getUser().getLastName());
            } else {
                doctorGreetingLabel.setText("Hello, Doctor");
            }
        }

        setupTableColumns();
        loadAppointments();

        // Initially disable row-dependent buttons
        if (cancelAppointmentButton != null) cancelAppointmentButton.setDisable(true);
        if (deleteAppointmentButton != null) deleteAppointmentButton.setDisable(true);
        if (makeAvailableButton != null) makeAvailableButton.setDisable(true);

        // Guard against null appointmentsTable (in case FXML id is wrong)
        if (appointmentsTable != null) {
            appointmentsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
                boolean hasSelection = newSel != null;
                if (cancelAppointmentButton != null) cancelAppointmentButton.setDisable(!hasSelection);
                if (deleteAppointmentButton != null) deleteAppointmentButton.setDisable(!hasSelection);
                if (makeAvailableButton != null) makeAvailableButton.setDisable(!hasSelection);
            });
        } else {
            System.err.println("WARNING: appointmentsTable is null (check fx:id in FXML)");
        }
    }

    // -------- bottom buttons --------

    @FXML
    private void handleMakeAvailable() {
        Appointment selected = appointmentsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Make appointment available");
        confirm.setHeaderText("Make this appointment available to patients again?");
        confirm.setContentText("Date/time: " + selected.getDate() +
                "\nCurrent status: " + selected.getStatus());

        if (confirm.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) {
            return;
        }

        try {
            selected.setPatient(null);
            selected.setStatus("AVAILABLE");
            AppointmentService.updateAppointment(selected);
            loadAppointments();
        } catch (Exception e) {
            e.printStackTrace();
            showError("Could not make appointment available", e);
        }
    }

    @FXML
    private void handleDeleteAppointment() {
        Appointment selected = appointmentsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete appointment");
        confirm.setHeaderText("Delete selected appointment?");
        confirm.setContentText(
                "Date/time: " + selected.getDate() +
                        "\nStatus: " + selected.getStatus() +
                        "\n\nThis cannot be undone."
        );

        if (confirm.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) {
            return;
        }

        try {
            AppointmentService.deleteAppointment(selected.getId());
            loadAppointments();
        } catch (Exception e) {
            e.printStackTrace();
            showError("Could not delete appointment", e);
        }
    }

    @FXML
    private void handleCancelAppointment() {
        Appointment selected = appointmentsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            return; // No selection; button should be disabled anyway
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Cancel appointment");
        confirm.setHeaderText("Cancel selected appointment?");
        confirm.setContentText("Date/time: " + selected.getDate() + "\nStatus: " + selected.getStatus());
        Optional<ButtonType> result = confirm.showAndWait();

        if (result.isEmpty() || result.get() != ButtonType.OK) {
            return; // User cancelled
        }

        try {
            selected.setPatient(null);
            selected.setStatus("CANCELLED");
            AppointmentService.updateAppointment(selected);
            loadAppointments();
        } catch (Exception e) {
            e.printStackTrace();
            showError("Could not cancel appointment", e);
        }
    }

    // -------- Create appointment (same as before) --------

    @FXML
    private void onCreateAppointmentClicked() {
        try {
            TextInputDialog dateDialog = new TextInputDialog();
            dateDialog.setTitle("Create available appointment");
            dateDialog.setHeaderText("Enter date and time for the available slot");
            dateDialog.setContentText("Format: yyyy-MM-dd HH:mm");

            Optional<String> dateResult = dateDialog.showAndWait();
            if (dateResult.isEmpty()) {
                return; // user cancelled
            }

            String dateTimeInput = dateResult.get().trim();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime dateTime;

            try {
                dateTime = LocalDateTime.parse(dateTimeInput, formatter);
            } catch (DateTimeParseException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid date/time");
                alert.setHeaderText(null);
                alert.setContentText("Please use format: yyyy-MM-dd HH:mm");
                alert.showAndWait();
                return;
            }

            TextInputDialog notesDialog = new TextInputDialog();
            notesDialog.setTitle("Appointment notes");
            notesDialog.setHeaderText("Optional: describe the appointment");
            notesDialog.setContentText("Notes / reason:");

            String notes = notesDialog.showAndWait().orElse("").trim();

            if (appState.getUser() instanceof users.Doctor doctor) {
                doctor.createAvailableSlot(dateTime, notes);
            } else {
                int doctorId = appState.getUserId();
                DoctorService.createAvailableSlot(doctorId, dateTime, notes);
            }

            loadAppointments();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Available appointment created for " + dateTime.format(formatter));
            alert.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
            showError("Could not create appointment", e);
        }
    }

    // -------- Filters + table setup --------

    private void setupTableColumns() {
        if (dateColumn != null) {
            dateColumn.setCellValueFactory(cell -> {
                Appointment appt = cell.getValue();
                LocalDateTime dt = appt.getDate();
                String dateStr = dt != null ? dt.format(dateFmt) : "";
                return new SimpleStringProperty(dateStr);
            });
        }

        if (timeColumn != null) {
            timeColumn.setCellValueFactory(cell -> {
                Appointment appt = cell.getValue();
                LocalDateTime dt = appt.getDate();
                String timeStr = dt != null ? dt.format(timeFmt) : "";
                return new SimpleStringProperty(timeStr);
            });
        }

        if (patientColumn != null) {
            patientColumn.setCellValueFactory(cell -> {
                Appointment appt = cell.getValue();
                if (appt.getPatient() == null) {
                    return new SimpleStringProperty("(None)");
                } else {
                    String fullName = appt.getPatient().getName() + " " + appt.getPatient().getLastName();
                    return new SimpleStringProperty(fullName);
                }
            });
        }

        if (reasonColumn != null) {
            reasonColumn.setCellValueFactory(cell -> {
                String notes = cell.getValue().getNotes();
                return new SimpleStringProperty(notes != null ? notes : "");
            });
        }

        if (statusColumn != null) {
            statusColumn.setCellValueFactory(cell -> {
                String status = cell.getValue().getStatus();
                return new SimpleStringProperty(status != null ? status : "");
            });
        }
    }

    private void loadAppointments() {
        if (appointmentsTable == null) {
            System.err.println("loadAppointments(): appointmentsTable is null");
            return;
        }

        try {
            int doctorId = appState.getUserId();
            List<Appointment> appointments = DoctorService.fetchAppointmentsForDoctorFiltered(
                    doctorId, null, null, null
            );
            appointmentsTable.setItems(FXCollections.observableArrayList(appointments));
        } catch (Exception e) {
            e.printStackTrace();
            showError("Failed to load appointments.", e);
        }
    }

    @FXML
    private void handleFilter() {
        if (appointmentsTable == null) return;

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
            Alert a = new Alert(Alert.AlertType.WARNING, "Invalid patient ID. Please enter a number.", ButtonType.OK);
            a.show();
        } catch (Exception e) {
            e.printStackTrace();
            showError("Failed to fetch appointments.", e);
        }
    }

    @FXML
    private void handleReset() {
        loadAppointments();
        if (startDatePicker != null) startDatePicker.setValue(null);
        if (endDatePicker != null) endDatePicker.setValue(null);
        if (patientIdField != null) patientIdField.clear();
    }

    // -------- helpers --------

    private void showError(String header, Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText(e.getMessage());
        alert.showAndWait();
    }

    // -------- top navbar handlers --------

    @FXML
    private void handleAppointmentsButton() {
        System.out.println("Doctor clicked My Appointments (top navbar)");
        // If you have a dedicated appointments screen, keep this:
        screenManager.show("doctorAppointmentsPage.fxml");
        // Otherwise, change the FXML or this target later as needed.
    }

    @FXML
    private void handleProfileButton() {
        System.out.println("Doctor clicked Profile (top navbar)");
        screenManager.show("doctorProfilePage.fxml");
    }

    // -------- profile menu actions --------

    @FXML
    private void handleLogout() {
        // Clear in-memory session
        appState.setUser(null);
        screenManager.show("login.fxml");
    }

    @FXML
    private void handleDeleteAccount() {
        if (appState.getUser() == null) {
            showError("No logged in user.", new IllegalStateException("No user"));
            return;
        }

        int userId = appState.getUser().getId();

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete Account");
        confirm.setHeaderText("Are you sure you want to delete your account?");
        confirm.setContentText(
                "This will:\n" +
                        "• Delete ALL your appointments (as doctor or patient)\n" +
                        "• Permanently delete your account\n\n" +
                        "This action cannot be undone."
        );

        if (confirm.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) {
            return;
        }

        try {
            // 1) Delete all appointments tied to this user
            AppointmentService.deleteAllAppointmentsForUser(userId);

            // 2) Delete the user row
            UserService.deleteUser(userId);

            // 3) Clear session and go to login
            appState.setUser(null);
            screenManager.show("loginPage.fxml");

        } catch (Exception e) {
            e.printStackTrace();
            showError("Account deletion failed.", e);
        }
    }
}