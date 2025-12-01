package core;

import controllers.BaseController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ScreenManager {
    private Stage stage;

    public ScreenManager(Stage stage){
        this.stage = stage;
    }

    public void show(String fxmlName){
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/screens/" + fxmlName)
            );

            Parent root = loader.load();
            BaseController controller = loader.getController();
            controller.setScreenManager(this);
            stage.setScene(new Scene(root));
            stage.show();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
