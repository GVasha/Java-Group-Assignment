module com.example.javagroupassignment {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    // requires com.example.javagroupassignment;

    requires java.net.http;
    requires com.google.gson;

    opens com.example.javagroupassignment to javafx.fxml;
    exports com.example.javagroupassignment;
}