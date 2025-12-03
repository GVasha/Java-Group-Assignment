package controllers;

import core.AppState;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import users.User;

import static utils.MessageUtils.*;

public class ModifyAccountController extends BaseController {

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
        // Basic validation
        if(!password.isEmpty() || !confirmPassword.isEmpty()){
            if (!password.equals(confirmPassword)) {
                showError(formMessage, "Provided passwords do not match!");
            }
            else {
                try {
                   currentUser.setPassword(password);
                } catch(Exception e){
                    e.printStackTrace();
                }
                showSuccess(formMessage, "Successfully modified credentials!");

            }
        }

        if(!email.isEmpty()) {
            // TODO: VALIDATE EMAIL!
           try {
               currentUser.setEmail(email);
           } catch (Exception e){
               e.printStackTrace();
           }
           showSuccess(formMessage, "Successfully modified credentials!");
        }

    }

    @FXML
    private void handleCancelButton() {
        boolean isPatient = appState.getUser().getSpecialization().equals("none");
        String fxmlFile = isPatient ? "patientLandingPage.fxml" : "doctorLandingPage.fxml";
        screenManager.show(fxmlFile);
    }

}
