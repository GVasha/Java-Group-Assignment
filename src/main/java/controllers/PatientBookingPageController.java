package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class PatientBookingPageController extends BaseController {

    @FXML private DatePicker appointmentDatePicker;
    @FXML private TextField appointmentTimeField;
    @FXML private TextField doctorIdField;
    @FXML private ComboBox<String> specializationCombo;
    @FXML private TextArea notesTextArea;

    @FXML
    public void initialize() {
        specializationCombo.getItems().addAll(
                "Cardiology",
                "Dermatology",
                "Neurology",
                "Pediatrics",
                "General Medicine"
        );
    }

    @FXML
    private void handleBackButton() {
        screenManager.show("patientLandingPage.fxml");
    }

    @FXML
    private void handleCancelButton() {
        screenManager.show("patientLandingPage.fxml");
    }

    @FXML
    private void handleSubmitButton() {
        // TODO: Implement booking logic
        System.out.println("Submit button clicked!");
    }
}

