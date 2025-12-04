package utils;

import javafx.scene.control.Alert;
import javafx.scene.text.Text;

public class MessageUtils {

    // Function for error message in the UI (not alert)
    public static void showError(Text target, String message) {
        target.setText(message);
        target.setStyle("-fx-fill: #ef4444; -fx-font-weight: bold;"); // Red
        target.setVisible(true);
    }

    // Overloaded function for on-screen alerts!
    public static void showError(String header, Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText(e.getMessage());
        alert.showAndWait();
    }

    public static void showSuccess(Text target, String message) {
        target.setText(message);
        target.setStyle("-fx-fill: #22c55e; -fx-font-weight: bold;"); // Green
        target.setVisible(true);
    }

    public static void clearMessage(Text target) {
        target.setVisible(false);
        target.setText("");
    }
}
