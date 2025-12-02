package controllers;

import core.ScreenManager;
import core.AppState;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;

public class DoctorPageController extends BaseController {

    @FXML private Button bookButton;
    @FXML private Button appointmentsButton;
    @FXML private Button profileButton;

    @FXML private TableView<?> appointmentsTable;
    @FXML private TableColumn<?, ?> dateColumn;
    @FXML private TableColumn<?, ?> timeColumn;
    @FXML private TableColumn<?, ?> patientColumn;
    @FXML private TableColumn<?, ?> reasonColumn;
    @FXML private TableColumn<?, ?> statusColumn;

    private final AppState appState = AppState.getInstance();

    @FXML
    public void initialize() {
        System.out.println("DoctorPageController initialized for user ID: " + appState.getUserId());
        // Maybe load appointments here!
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
}
