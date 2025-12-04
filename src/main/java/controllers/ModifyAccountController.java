package controllers;

import core.AppState;
import database_management.AppointmentService;
import database_management.UserService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import users.User;
import utils.Validator;

import static utils.MessageUtils.*;

public class ModifyAccountController extends BaseController {

    @FXML private TextField emailField;
    @FXML private Text passwordFormMessage;
    @FXML private Text emailFormMessage;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;

    private final AppState appState = AppState.getInstance();

    @FXML
    private void handleSaveButton() {
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        User currentUser = appState.getUser();
        boolean success = false;
        // Basic validation
        if(!password.isEmpty() || !confirmPassword.isEmpty()){
            if (!password.equals(confirmPassword)) {
                showError(passwordFormMessage, "Provided passwords do not match!");
            }
            else {
                try {
                   currentUser.setPassword(password);
                   success = true;
                } catch(Exception e){
                    e.printStackTrace();
                }
                if(success) {
                    showSuccess(passwordFormMessage, "Successfully modified password!");
                }
                if(!email.isEmpty()) {
                    if(!Validator.isValidEmailFormat(email)){
                        showError(emailFormMessage, "Invalid email format!");
                    }
                    else {
                        success = false; // refresh success if also changing email
                        try {
                            User taken_email = UserService.fetchUser(email);
                            if(taken_email == null){
                                currentUser.setEmail(email);
                                success = true;
                            }
                            else{
                                showError(emailFormMessage, "Email is already taken!");
                            }
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                        if(success){
                            showSuccess(emailFormMessage, "Successfully modified email!");
                        }
                    }
                }
            }
        }
        else{
            if(!email.isEmpty()) {
                if(!Validator.isValidEmailFormat(email)){
                    showError(emailFormMessage, "Invalid email format!");
                }
                else {
                success = false; // refresh success if also changing email
                try {
                    User taken_email = UserService.fetchUser(email);
                    if(taken_email == null){
                        currentUser.setEmail(email);
                        success = true;
                    }
                    else{
                        showError(emailFormMessage, "Email is already taken!");
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
                if(success){
                    showSuccess(emailFormMessage, "Successfully modified email!");
                }
                }
            }
        }
    }

    private void goToHomePage(){
        boolean isPatient = appState.getUser().getSpecialization().equals("none");
        String fxmlFile = isPatient ? "patientLandingPage.fxml" : "doctorLandingPage.fxml";
        screenManager.show(fxmlFile);
    }

    @FXML
    private void handleCancelButton() {
        goToHomePage();
    }

    @FXML
    private void handleDeleteAccount() {
        if (appState.getUser() == null) {
            showError(emailFormMessage,"No logged in user.");
            return;
        }

        User user = appState.getUser();
        int userId = user.getId();

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
            boolean isPatient = user.getSpecialization().equals("none");
            if (isPatient) {
                // Releasing slots for patients / Cant delete appointment for patients!
                AppointmentService.releaseAppointmentsForPatient(userId);
            } else {
                // Doctors can delete the appointments FULLY!
                AppointmentService.deleteAllAppointmentsForUser(userId);
            }

            UserService.deleteUser(userId);

            appState.setUser(null);
            screenManager.show("login.fxml");

        } catch (Exception e) {
            e.printStackTrace();
            showError(emailFormMessage,"Account deletion failed.");
        }
    }
}
