module compumart.compumart {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;  // Add this line

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires org.mongodb.driver.core;
    requires org.mongodb.bson;
    requires org.mongodb.driver.sync.client;
    requires javafx.graphics;
    requires javafx.base;

    opens compumart.compumart to javafx.fxml;
    opens compumart.compumart.controller to javafx.fxml;
    opens compumart.compumart.model to javafx.fxml, org.mongodb.bson;

    exports compumart.compumart;
    exports compumart.compumart.controller;
    exports compumart.compumart.model;
}