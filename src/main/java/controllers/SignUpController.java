package controllers;

import authentication.Authentication;
import core.AppState;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import users.Patient;


public class SignUpController extends BaseController {

    @FXML private VBox patientForm;
    @FXML private VBox doctorForm;
    @FXML private Button patientTab;
    @FXML private Button doctorTab;

    // Input areas
    @FXML private TextField patientName;
    @FXML private TextField patientLastName;
    @FXML private TextField patientEmail;
    @FXML private PasswordField patientPassword;
    @FXML private PasswordField patientConfirmPassword;


    @FXML private TextField doctorName;
    @FXML private TextField doctorLastName;
    @FXML private TextField doctorEmail;
    @FXML private PasswordField doctorPassword;
    @FXML private PasswordField doctorConfirmPassword;

    AppState appState = AppState.getInstance();

    @FXML private Text formMessage;

    // Some helper functions to manage error/success messages
    // TODO: make it reusable also for login or any other views that need error or success messages!
    private void showError(String message) {
        formMessage.setText(message);
        formMessage.setStyle("-fx-fill: #ef4444; -fx-font-weight: bold;"); // Red
        formMessage.setVisible(true);
    }

    private void showSuccess(String message) {
        formMessage.setText(message);
        formMessage.setStyle("-fx-fill: #22c55e; -fx-font-weight: bold;"); // Green
        formMessage.setVisible(true);
    }

    private void clearMessage() {
        formMessage.setVisible(false);
        formMessage.setText("");
    }


    private void activateTab(Button active, Button inactive) {
        active.setStyle("-fx-background-color: #3b82f6; -fx-text-fill: white; -fx-background-radius: 999;");
        inactive.setStyle("-fx-background-color: transparent; -fx-text-fill: #cbd5f5; -fx-border-color: #64748b; -fx-border-radius: 999;");
    }

    @FXML
    private void handlePatientSignUp(ActionEvent event) {
        System.out.println("Patient Sign Up clicked!");
        String patientName = this.patientName.getText();
        String patientLastName = this.patientLastName.getText();
        String patientEmail = this.patientEmail.getText();
        String patientPassword = this.patientPassword.getText();
        String patientConfirmPassword = this.patientConfirmPassword.getText();
        if(patientName.isEmpty() ||
                patientLastName.isEmpty() ||
                patientEmail.isEmpty() ||
                patientPassword.isEmpty() ||
                patientConfirmPassword.isEmpty()
        ){
            showError("Something is missing in the form!");
            return;
        }
        if(!patientPassword.equals(patientConfirmPassword)){
            showError("Passwords do not match!");
            return;
        }
        try{
            Patient newPatient = Authentication.patientSignUp(patientEmail, patientName, patientLastName, patientPassword);
            if(newPatient == null){
                showError("Problem signing up the user!");
            }
            else{
                showSuccess("Successfully created the user!");
                // TODO: Potentially change app state to store User object not only ID
                appState.setUserId(newPatient.getId());
                screenManager.show("patientLandingPage.fxml");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDoctorSignUp(ActionEvent event) {
        System.out.println("Doctor Sign Up clicked!");
        // TODO: validate sign up
    }

    @FXML
    private void showDoctorForm(ActionEvent event) {
        System.out.println("Clicked on the doctor form button!");
        patientForm.setVisible(false);
        patientForm.setManaged(false);
        doctorForm.setVisible(true);
        doctorForm.setManaged(true);

        activateTab(doctorTab, patientTab);
        clearMessage();
    }

    @FXML
    private void showPatientForm(ActionEvent event) {
        System.out.println("Clicked on the patient form button!");
        doctorForm.setVisible(false);
        doctorForm.setManaged(false);
        patientForm.setVisible(true);
        patientForm.setManaged(true);

        activateTab(patientTab, doctorTab);
        clearMessage();
    }

    @FXML
    private void handleGoToLogin(ActionEvent event) {
        System.out.println("Go to login clicked!");
        screenManager.show("login.fxml");
    }
}
