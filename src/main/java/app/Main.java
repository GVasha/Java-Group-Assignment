package app;

import core.ScreenManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        ScreenManager screenManager = new ScreenManager(stage);
        screenManager.show("login.fxml");
    }


}
