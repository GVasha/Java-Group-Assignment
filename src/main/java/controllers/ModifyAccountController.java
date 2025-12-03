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

import static utils.MessageUtils.*;

public class ModifyAccountController extends BaseController {

    private final static int WAIT_BEFORE_CLOSE = 2000;
    @FXML
    private TextField emailField;

    @FXML
    private Text formMessage;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

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
                showError(formMessage, "Provided passwords do not match!");
            }
            else {
                try {
                   currentUser.setPassword(password);
                   success = true;
                } catch(Exception e){
                    e.printStackTrace();
                }
                if(success) {
                    showSuccess(formMessage, "Successfully modified credentials!");
                }
                // TODO: modularize this part?
                if(!email.isEmpty()) {
                    success = false; // refresh success if also changing email
                    // TODO: VALIDATE EMAIL!
                    try {
                        currentUser.setEmail(email);
                        success = true;
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                    if(success){
                        showSuccess(formMessage, "Successfully modified credentials!");
                    }
                }
            }
        }
        else{
            if(!email.isEmpty()) {
                success = false; // refresh success if also changing email
                // TODO: VALIDATE EMAIL!
                try {
                    currentUser.setEmail(email);
                    success = true;
                } catch (Exception e){
                    e.printStackTrace();
                }
                if(success){
                    showSuccess(formMessage, "Successfully modified credentials!");
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
            showError(formMessage,"No logged in user.");
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
            // TODO: Make appointments available for patients and delete for doctors
            AppointmentService.deleteAllAppointmentsForUser(userId);

            // 2) Delete the user row
            UserService.deleteUser(userId);

            appState.setUser(null);
            screenManager.show("login.fxml");

        } catch (Exception e) {
            e.printStackTrace();
            showError(formMessage,"Account deletion failed.");
        }
    }
}
