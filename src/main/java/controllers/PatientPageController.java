package controllers;

import appointments.Appointment;
import core.AppState;
import database_management.PatientService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import users.Doctor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PatientPageController extends BaseController {

    @FXML private Button bookButton;
    @FXML private Button appointmentsButton;
    @FXML private Button profileButton;

    @FXML private FlowPane appointmentsGrid;

    private final AppState appState = AppState.getInstance();

    private final DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("HH:mm");
    private final DateTimeFormatter dateTimeFmt = DateTimeFormatter.ofPattern("MMM dd, yyyy");

    @FXML private Label patientGreetingLabel;

    public void initialize() {
        System.out.println("PatientPageController initialized for user ID: " + appState.getUserId());

        patientGreetingLabel.setText("Hello, " + appState.getUser().getName());
        loadAppointments();
    }

    @FXML
    private void handleBookButton() {
        System.out.println("Book button clicked!");
        screenManager.show("PatientBookingPage.fxml");
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

    @FXML
    private void handleLogout() {
        // Clear in-memory session
        appState.setUser(null);
        screenManager.show("login.fxml");
    }

    public void handleModifyAccount(ActionEvent actionEvent) {
        screenManager.show("modifyAccount.fxml");
    }

    private void loadAppointments() {
        try {
            int patientId = appState.getUserId();
            List<Appointment> appointments = PatientService.fetchAppointmentsForPatientFiltered(
                    patientId, null, null, null, null
            );

            appointmentsGrid.getChildren().clear();

            for (Appointment appt : appointments) {
                VBox card = createAppointmentCard(appt);
                appointmentsGrid.getChildren().add(card);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private VBox createAppointmentCard(Appointment appt) {
        VBox card = new VBox();
        card.setSpacing(12);
        card.setPrefWidth(280);
        card.setStyle("-fx-background-color: #ffffff; " +
                "-fx-background-radius: 16; " +
                "-fx-padding: 24; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 12, 0, 0, 4); " +
                "-fx-border-color: #e2e8f0; " +
                "-fx-border-width: 1; " +
                "-fx-border-radius: 16;");

        // Date label
        Label dateLabel = new Label();
        LocalDateTime dt = appt.getDate();
        if (dt != null) {
            String dateStr = dt.format(dateTimeFmt) + " at " + dt.format(timeFmt);
            dateLabel.setText(dateStr);
        } else {
            dateLabel.setText("Date: Not set");
        }
        dateLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");

        // Doctor label
        Label doctorLabel = new Label();
        Doctor doctor = appt.getDoctor();
        if (doctor != null) {
            doctorLabel.setText("Dr. " + doctor.getName());
        } else {
            doctorLabel.setText("Doctor: Not assigned");
        }
        doctorLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #475569;");

        // Specialization label
        Label specializationLabel = new Label();
        if (doctor != null) {
            String specialization = doctor.getSpecialization();
            if (specialization != null && !specialization.isEmpty() && !specialization.equals("none")) {
                specializationLabel.setText("Specialization: " + specialization);
                specializationLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #64748b;");
            } else {
                specializationLabel.setText("Specialization: General");
                specializationLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #94a3b8; -fx-font-style: italic;");
            }
        } else {
            specializationLabel.setText("Specialization: N/A");
            specializationLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #94a3b8; -fx-font-style: italic;");
        }

        // Status label with color coding
        Label statusLabel = new Label();
        String status = appt.getStatus();
        if (status != null && !status.isEmpty()) {
            statusLabel.setText("Status: " + status);
            String statusColor;
            switch (status.toUpperCase()) {
                case "SCHEDULED":
                    statusColor = "#3b82f6"; // Blue
                    break;
                case "COMPLETED":
                    statusColor = "#10b981"; // Green
                    break;
                case "CANCELLED":
                    statusColor = "#ef4444"; // Red
                    break;
                case "AVAILABLE":
                    statusColor = "#8b5cf6"; // Purple
                    break;
                default:
                    statusColor = "#64748b"; // Gray
            }
            statusLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: 600; -fx-text-fill: " + statusColor + ";");
        } else {
            statusLabel.setText("Status: Unknown");
            statusLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: 600; -fx-text-fill: #64748b;");
        }

        // Notes label
        Label notesLabel = new Label();
        String notes = appt.getNotes();
        if (notes != null && !notes.trim().isEmpty()) {
            notesLabel.setText("Notes: " + notes);
            notesLabel.setWrapText(true);
            notesLabel.setMaxWidth(232); // Card width minus padding
            notesLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #64748b;");
        } else {
            notesLabel.setText("Notes: None");
            notesLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #94a3b8; -fx-font-style: italic;");
        }

        card.getChildren().addAll(dateLabel, doctorLabel, specializationLabel, statusLabel, notesLabel);

        return card;
    }
}