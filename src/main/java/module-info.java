module app {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.net.http;
    requires com.google.gson;

    opens app to javafx.fxml;
    exports app;

    opens controllers to javafx.fxml;
    exports controllers;

    opens core to javafx.fxml;
    exports core;
}
