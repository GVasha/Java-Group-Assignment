package controllers;

import core.AppState;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import users.User;

public class ModifyAccountController extends BaseController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    private final AppState appState = AppState.getInstance();

    @FXML
    public void initialize() {
        // TODO: Load current account data, e.g.:
        // emailField.setText(currentUser.getEmail());
    }

    @FXML
    private void handleSaveButton() {
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Basic validation
        if (email.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Missing Information", "Email cannot be empty.");
            return;
        }

        if (!password.isEmpty() || !confirmPassword.isEmpty()) {
            if (!password.equals(confirmPassword)) {
                showAlert(Alert.AlertType.WARNING, "Password Mismatch", "Passwords do not match.");
                return;
            }

            if (password.length() < 6) {
                showAlert(Alert.AlertType.WARNING, "Invalid Password", "Password must contain at least 6 characters.");
                return;
            }
        }

        // TODO: Save changes to database

        showAlert(Alert.AlertType.INFORMATION, "Success", "Account information updated successfully.");

        closeWindow();
    }

    @FXML
    private void handleCancelButton() {
        boolean isPatient = appState.getUser().getSpecialization().equals("none");
        String fxmlFile = isPatient ? "patientLandingPage.fxml" : "doctorLandingPage.fxml";
        screenManager.show(fxmlFile);
    }



    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Closes the current window.
     */
    private void closeWindow() {
        Stage stage = (Stage) emailField.getScene().getWindow();
        stage.close();
    }
}
