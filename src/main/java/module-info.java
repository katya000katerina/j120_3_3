module org.programmercalculator {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.programmercalculator to javafx.fxml;
    exports org.programmercalculator;
}