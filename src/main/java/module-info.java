module compumart.compumart {
    requires javafx.controls;
    requires javafx.fxml;


    opens compumart.compumart to javafx.fxml;
    exports compumart.compumart;
}