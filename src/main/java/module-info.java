module com.example.javagroupassignment {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.javagroupassignment to javafx.fxml;
    exports com.example.javagroupassignment;
}