package controllers;

import core.AppState;
import javafx.fxml.FXML;
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

}
