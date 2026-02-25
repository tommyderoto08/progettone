module com.example.progettone {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.progettone to javafx.fxml;
    exports com.example.progettone;
    exports com.example.progettone.controller;
    opens com.example.progettone.controller to javafx.fxml;
}