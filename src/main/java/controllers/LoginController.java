package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController extends BaseController {

    @FXML
    private TextField usernameSignUp;

    @FXML
    private TextField emailSignUp;

    @FXML
    private PasswordField passwordSignUp;

    @FXML
    public void handleSignUpSubmit(){
        System.out.println("PRESSED BUTTON!");
        String username = usernameSignUp.getText();
        String email = emailSignUp.getText();
        String password = passwordSignUp.getText();
        System.out.println(username + " " + email + " " + password);
    }
}
