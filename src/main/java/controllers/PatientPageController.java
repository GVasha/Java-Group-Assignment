package controllers;

import core.ScreenManager;
import core.AppState;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class PatientPageController extends BaseController {

    @FXML private Button bookButton;
    @FXML private Button appointmentsButton;
    @FXML private Button profileButton;

    private final AppState appState = AppState.getInstance();

    @FXML
    public void initialize() {
        // Called automatically after FXML loads
        // You can load patient data here if needed
        System.out.println("PatientPageController initialized for user ID: " + appState.getUserId());
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
}
