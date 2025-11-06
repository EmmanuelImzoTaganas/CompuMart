module compumart.compumart {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens compumart.compumart to javafx.fxml;
    exports compumart.compumart;
    exports compumart.compumart.model;
    opens compumart.compumart.model to javafx.fxml;
    exports compumart.compumart.service;
    opens compumart.compumart.service to javafx.fxml;
}