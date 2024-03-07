module com.example.horse {
    requires javafx.controls;
    requires javafx.fxml;

    exports com.example.horse;
    opens com.example.horse to javafx.fxml;
    exports com.example.horse.base;
    opens com.example.horse.base to javafx.fxml;
    exports com.example.horse.Controller;
    opens com.example.horse.Controller to javafx.fxml;
}