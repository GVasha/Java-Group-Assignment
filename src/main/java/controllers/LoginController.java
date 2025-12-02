package controllers;

import authentication.Authentication;
import core.AppState;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.text.Text;
import javafx.scene.control.TextField;
import users.User;

import static utils.MessageUtils.*;

public class LoginController extends BaseController {

    @FXML private TextField emailLogin;
    @FXML private PasswordField passwordLogin;

    @FXML private Text formMessage;

    private final AppState appState = AppState.getInstance();

    @FXML
    public void initialize() {
        System.out.println("LoginController initialized");
    }

    @FXML
    private void handleLogin() {
        clearMessage(formMessage);

        String email = emailLogin.getText();
        String password = passwordLogin.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showError(formMessage,"Please enter both email and password.");
            return;
        }

        try {
            User user = Authentication.logIn(email, password);

            if (user == null) {
                showError(formMessage,"Invalid email or password!");
                return;
            }

            showSuccess(formMessage,"Login successful!");

            appState.setUser(user);

            if (user.getSpecialization().equals("none")) {
                screenManager.show("patientLandingPage.fxml");
            } else {
                screenManager.show("doctorLandingPage.fxml");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showError(formMessage,"An unexpected error occurred.");
        }
    }

    @FXML
    private void handleGoToSignUp() {
        clearMessage(formMessage);
        screenManager.show("signup.fxml");
    }

}
