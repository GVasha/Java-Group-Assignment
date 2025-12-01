package controllers;

import core.ScreenManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class LoginController extends BaseController {

    private ScreenManager screenManager;

    public void setScreenManager(ScreenManager screenManager) {
        this.screenManager = screenManager;
    }

    @FXML
    private void handleSignIn(ActionEvent event) {
        System.out.println("Login clicked!");
        // TODO: validate login
    }

    @FXML
    private void handleGoToSignUp(ActionEvent event) {
        // TODO: implement redirection
        System.out.println("Go to signup clicked!");
        screenManager.show("signup.fxml");
    }
}
