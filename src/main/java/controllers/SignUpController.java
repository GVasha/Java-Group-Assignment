package controllers;

import authentication.Authentication;
import core.AppState;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import users.Doctor;
import users.Patient;

import static utils.MessageUtils.*;


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

    @FXML
    private ComboBox<String> doctorSpecialization;

    @FXML
    public void initialize() {
        doctorSpecialization.getItems().addAll(
                "Cardiology",
                "Dermatology",
                "Neurology",
                "Pediatrics",
                "General Medicine"
        );
    }

    AppState appState = AppState.getInstance();

    @FXML private Text formMessage;

    /**
     * Validates email format using regex pattern
     * @param email The email string to validate
     * @return true if email format is valid, false otherwise
     */
    private boolean isValidEmailFormat(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        // Standard email regex pattern
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    private void activateTab(Button active, Button inactive) {
        active.setStyle("-fx-background-color: #3b82f6; -fx-text-fill: white; -fx-background-radius: 999;");
        inactive.setStyle("-fx-background-color: transparent; -fx-text-fill: #cbd5f5; -fx-border-color: #64748b; -fx-border-radius: 999;");
    }

    @FXML
    private void handleDoctorSignUp(ActionEvent event) {
        System.out.println("Doctor Sign Up clicked!");
        String doctorName = this.doctorName.getText();
        String doctorLastName = this.doctorLastName.getText();
        String doctorEmail = this.doctorEmail.getText();
        String doctorPassword = this.doctorPassword.getText();
        String doctorConfirmPassword = this.doctorConfirmPassword.getText();

        if (doctorName.isEmpty() ||
                doctorLastName.isEmpty() ||
                doctorEmail.isEmpty() ||
                doctorPassword.isEmpty() ||
                doctorConfirmPassword.isEmpty()
        ) {
            showError(formMessage,"Something is missing in the form!");
            return;
        }

        if (!isValidEmailFormat(doctorEmail)) {
            showError(formMessage,"Please enter a valid email address!");
            return;
        }

        if (doctorSpecialization.getValue() == null) {
            showError(formMessage,"Please select a specialization.");
            return;
        }

        if (!doctorPassword.equals(doctorConfirmPassword)) {
            showError(formMessage,"Passwords do not match!");
            return;
        }

        try {
            Doctor newDoctor = Authentication.doctorSignUp(
                    doctorEmail,
                    doctorName,
                    doctorLastName,
                    doctorPassword,
                    doctorSpecialization.getValue()
            );

            if (newDoctor == null) {
                showError(formMessage,"Problem signing up the doctor!");
            } else {
                showSuccess(formMessage,"Successfully created the doctor!");
                appState.setUser(newDoctor);
                screenManager.show("doctorLandingPage.fxml");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
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
            showError(formMessage,"Something is missing in the form!");
            return;
        }

        if (!isValidEmailFormat(patientEmail)) {
            showError(formMessage,"Please enter a valid email address!");
            return;
        }

        if(!patientPassword.equals(patientConfirmPassword)){
            showError(formMessage,"Passwords do not match!");
            return;
        }
        try{
            Patient newPatient = Authentication.patientSignUp(patientEmail, patientName, patientLastName, patientPassword);
            if(newPatient == null){
                showError(formMessage,"Problem signing up the user!");
            }
            else{
                showSuccess(formMessage,"Successfully created the user!");
                appState.setUser(newPatient);
                screenManager.show("patientLandingPage.fxml");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void showDoctorForm(ActionEvent event) {
        System.out.println("Clicked on the doctor form button!");
        patientForm.setVisible(false);
        patientForm.setManaged(false);
        doctorForm.setVisible(true);
        doctorForm.setManaged(true);

        activateTab(doctorTab, patientTab);
        clearMessage(formMessage);
    }

    @FXML
    private void showPatientForm(ActionEvent event) {
        System.out.println("Clicked on the patient form button!");
        doctorForm.setVisible(false);
        doctorForm.setManaged(false);
        patientForm.setVisible(true);
        patientForm.setManaged(true);

        activateTab(patientTab, doctorTab);
        clearMessage(formMessage);
    }

    @FXML
    private void handleGoToLogin(ActionEvent event) {
        System.out.println("Go to login clicked!");
        screenManager.show("login.fxml");
    }
}
