module com.example.towerdifence {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.towerdifence to javafx.fxml;
    exports com.example.towerdifence;
    exports com.example.towerdifence.controller;
    opens com.example.towerdifence.controller to javafx.fxml;
}