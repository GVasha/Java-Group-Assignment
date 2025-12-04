package controllers;

import appointments.Appointment;
import core.AppState;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import users.Doctor;
import users.Patient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static utils.MessageUtils.showError;

public class DoctorPageController extends BaseController {

    private final AppState appState = AppState.getInstance();
    Doctor doctor = (Doctor) appState.getUser(); // Gets logged user

    // Buttons
    @FXML private Button cancelAppointmentButton;
    @FXML private Button deleteAppointmentButton;
    @FXML private Button makeAvailableButton;
    @FXML private Button completeButton;

    // Table
    @FXML private TableView<Appointment> appointmentsTable;
    @FXML private TableColumn<Appointment, String> dateColumn;
    @FXML private TableColumn<Appointment, String> timeColumn;
    @FXML private TableColumn<Appointment, String> patientColumn;
    @FXML private TableColumn<Appointment, String> notesColumn;
    @FXML private TableColumn<Appointment, String> statusColumn;

    @FXML private PieChart appointmentBreakdownChart;

    // Filters
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private TextField patientNameField; // Used to input patient full name (first + last)
    @FXML private ComboBox<String> statusCombo;

    @FXML private Label doctorGreetingLabel;

    private final DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("HH:mm");


    // INITIALIZATION

    @FXML
    public void initialize(){
        System.out.println("DoctorPageController initialize(), userId = " + appState.getUserId());

        SetUpStatusCombo();
        setUpGreeting();
        setupTableColumns();
        setupSelectionListener();
        loadAppointments();
        initializeActionButtons();
    }

    private void SetUpStatusCombo(){
        statusCombo.getItems().addAll(
                "AVAILABLE",
                "SCHEDULED",
                "COMPLETED",
                "CANCELLED"
        );
        statusCombo.setValue("SCHEDULED");
    }

    private void setUpGreeting(){
        if (doctorGreetingLabel != null) {
            if (doctor != null) {
                doctorGreetingLabel.setText("Hello, Dr. " + appState.getUser().getLastName());
            } else {
                doctorGreetingLabel.setText("Hello, Doctor"); // This should never happen, but just in case
            }
        }
    }
    private void setupSelectionListener(){
        if (appointmentsTable == null) return;

        appointmentsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            boolean selected = newSel != null;
            setButtonsState(selected, newSel);
        });
    }

    private void initializeActionButtons(){
        if (cancelAppointmentButton != null) cancelAppointmentButton.setDisable(true);
        if (deleteAppointmentButton != null) deleteAppointmentButton.setDisable(true);
        if (makeAvailableButton != null) makeAvailableButton.setDisable(true);
        if (completeButton != null) completeButton.setDisable(true);
    }

    private void setButtonsState(boolean selected, Appointment selectedAppointment){
        if (cancelAppointmentButton != null)
            cancelAppointmentButton.setDisable(!selected || "CANCELLED".equals(selectedAppointment.getStatus()));

        if (deleteAppointmentButton != null)
            deleteAppointmentButton.setDisable(!selected);

        if (makeAvailableButton != null)
            makeAvailableButton.setDisable(!selected || !"CANCELLED".equals(selectedAppointment.getStatus()));

        if (completeButton != null)
            completeButton.setDisable(!selected || !"SCHEDULED".equals(selectedAppointment.getStatus()));
    }


    // DOCTOR'S APPOINTMENT MANAGEMENT BUTTONS

    @FXML
    private void handleMakeAvailable(){
        runAppointmentAction(
                "Make appointment available",
                "Make this appointment available again?",
                doctor::makeAppointmentAvailable);
    }

    @FXML
    private void handleDeleteAppointment(){
        runAppointmentAction(
                "Delete appointment",
                "Delete this appointment? This cannot be undone.",
                doctor::deleteAppointment
        );
    }

    @FXML
    private void handleCancelAppointment(){
        runAppointmentAction(
                "Cancel appointment",
                "Cancel the selected appointment?",
                doctor::cancelAppointment
        );
    }

    @FXML
    private void handleCompleteAppointment(){
        runAppointmentAction(
                "Complete appointment",
                "Mark appointment as completed?",
                doctor::completeAppointment
        );
    }

    private void runAppointmentAction(String title, String message, Consumer<Appointment> action){
        Appointment selectedAppointment = appointmentsTable.getSelectionModel().getSelectedItem();
        if (selectedAppointment == null) return;

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle(title);
        confirm.setHeaderText(title);
        confirm.setContentText(message);

        if (confirm.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) {
            return;
        }

        try {
            action.accept(selectedAppointment);
            loadAppointments();

        } catch (Exception e) {
            e.printStackTrace();
            showError("Error performing: " + title, e);
        }
    }

    @FXML
    private void handleCreateAppointment() {
        try {
            Dialog<LocalDateTime> dialog = new Dialog<>();
            dialog.setTitle("Create available appointment");
            dialog.setHeaderText("Select date and time for the available slot");

            ButtonType createButtonType = new ButtonType("Create", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(createButtonType, ButtonType.CANCEL);

            DatePicker datePicker = new DatePicker();

            Spinner<Integer> hourSpinner = new Spinner<>(0, 23, 9);
            hourSpinner.setEditable(true);

            Spinner<Integer> minuteSpinner = new Spinner<>(0, 59, 0, 5);
            minuteSpinner.setEditable(true);

            VBox content = new VBox(10);
            content.setPadding(new Insets(10));
            content.getChildren().addAll(
                    new Label("Date:"), datePicker,
                    new Label("Time:"),
                    new HBox(5, new Label("Hour:"), hourSpinner,
                            new Label("Minute:"), minuteSpinner)
            );

            dialog.getDialogPane().setContent(content);

            dialog.setResultConverter(button -> {
                if (button == createButtonType) {

                    if (datePicker.getValue() == null) {
                        showAlert("Invalid date", "Please select a date.");
                        return null;
                    }

                    LocalDate date = datePicker.getValue();

                    // Validation of time (Sets max or min value if not acceptable)
                    Integer hour, minute;

                    try {
                        hour = Integer.parseInt(hourSpinner.getEditor().getText().trim());
                        minute = Integer.parseInt(minuteSpinner.getEditor().getText().trim());
                    } catch (NumberFormatException ex) {
                        showAlert("Invalid time", "Hour and minute must be numbers.");
                        return null;
                    }

                    if (hour < 0 || hour > 23) {
                        showAlert("Invalid hour", "Hour must be between 0 and 23.");
                        return null;
                    }

                    if (minute < 0 || minute > 59) {
                        showAlert("Invalid minute", "Minute must be between 0 and 59.");
                        return null;
                    }

                    LocalDateTime dateTime = LocalDateTime.of(date, LocalTime.of(hour, minute));

                    // Can not make appointment in the past
                    if (dateTime.isBefore(LocalDateTime.now())) {
                        showAlert("Invalid date/time", "The appointment cannot be set in the past.");
                        return null;
                    }

                    return dateTime;
                }
                return null;
            });


            Optional<LocalDateTime> result = dialog.showAndWait();

            // If validation failed, result will be empty
            if (result.isEmpty()) {
                return;
            }

            LocalDateTime dateTime = result.get();


            TextInputDialog notesDialog = new TextInputDialog();
            notesDialog.setTitle("Appointment notes");
            notesDialog.setHeaderText("Optional: describe the appointment");
            notesDialog.setContentText("Notes / reason:");

            String notes = notesDialog.showAndWait().orElse("").trim();

            // Editing the appointment for the doctor instance + update database
            doctor.createAvailableSlot(dateTime, notes);
            loadAppointments();

            // Success
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
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

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }



    // Appointment table setup for the doctor

    private void setupTableColumns(){
        setUpDateCol();
        setUpTimeCol();
        setUpPatientCol();
        setUpNotesCol();
        setUpStatusCol();
    }

    private void setUpDateCol(){
        dateColumn.setCellValueFactory(cell -> {
            Appointment appointment = cell.getValue();
            LocalDateTime dt = appointment.getDate();
            String dateStr = dt != null ? dt.format(dateFmt) : "";
            return new SimpleStringProperty(dateStr);
        });
    }

    private void setUpTimeCol(){
        timeColumn.setCellValueFactory(cell -> {
            Appointment appointment = cell.getValue();
            LocalDateTime dt = appointment.getDate();
            String timeStr = dt != null ? dt.format(timeFmt) : "";
            return new SimpleStringProperty(timeStr);
        });
    }

    private void setUpPatientCol(){
        patientColumn.setCellValueFactory(cell -> {
            Appointment appointment = cell.getValue();
            Patient patient = appointment.getPatient();
            if (patient == null) {
                return new SimpleStringProperty("(None)");
            } else {
                String fullName = patient.getName();
                return new SimpleStringProperty(fullName);
            }
        });
    }

    private void setUpNotesCol(){
        notesColumn.setCellValueFactory(cell -> {
            String notes = cell.getValue().getNotes();
            return new SimpleStringProperty(notes != null ? notes : "");
        });
    }

    private void setUpStatusCol(){
        statusColumn.setCellValueFactory(cell -> {
            String status = cell.getValue().getStatus();
            return new SimpleStringProperty(status != null ? status : "");
        });
    }


    // LOAD AND FILTER APPOINTMENTS

    private void loadAppointments(){
        if (appointmentsTable == null) {
            return;
        }

        try {
            List<Appointment> appointments = doctor.getMyAppointments();
            appointmentsTable.setItems(FXCollections.observableArrayList(appointments));
            refreshAppointmentBreakdown(appointments);
        } catch (Exception e) {
            e.printStackTrace();
            showError("Failed to load appointments.", e);
        }
    }

    // Updating the pie chart with the appointments from the doctor instance (ONLY Scheduled/(Scheduled+Available))
    private void refreshAppointmentBreakdown(List<Appointment> source) {
        if (appointmentBreakdownChart == null || source == null) {
            return;
        }

        long scheduled = source.stream()
                .filter(appt -> "SCHEDULED".equalsIgnoreCase(appt.getStatus()))
                .count();
        long available = source.stream()
                .filter(appt -> "AVAILABLE".equalsIgnoreCase(appt.getStatus()))
                .count();

        appointmentBreakdownChart.getData().setAll(
                new PieChart.Data("Scheduled", scheduled),
                new PieChart.Data("Available", available)
        );
    }

    @FXML
    private void handleFilter() {
        try {
            LocalDateTime start = null;
            LocalDateTime end = null;
            String patientName = null;
            String status = null;
            // Optional user passed filters
            if (startDatePicker != null && startDatePicker.getValue() != null) {
                start = startDatePicker.getValue().atStartOfDay();
            }
            if (endDatePicker != null && endDatePicker.getValue() != null) {
                end = endDatePicker.getValue().atTime(23, 59, 59);
            }
            if (patientNameField != null && !patientNameField.getText().trim().isEmpty()) {
                patientName = patientNameField.getText().trim();
            }
            if (statusCombo != null && !statusCombo.getValue().trim().isEmpty()) {
                status = statusCombo.getValue().trim();
            }

            List<Appointment> filtered = doctor.getMyAppointments(start, end, patientName, status);

            appointmentsTable.setItems(FXCollections.observableArrayList(filtered));
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
        if (patientNameField != null) patientNameField.clear();
        if (statusCombo != null) statusCombo.setValue("SCHEDULED");
    }


    @FXML
    private void handleLogout(){
        // Clear in-memory session
        appState.setUser(null);
        screenManager.show("login.fxml");
    }

    @FXML
    public void handleModifyAccount(ActionEvent actionEvent) {
        screenManager.show("modifyAccount.fxml");
    }
}
