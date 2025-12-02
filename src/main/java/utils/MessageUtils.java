package utils;

import javafx.scene.text.Text;

public class MessageUtils {
    public static void showError(Text target, String message) {
        target.setText(message);
        target.setStyle("-fx-fill: #ef4444; -fx-font-weight: bold;"); // Red
        target.setVisible(true);
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
