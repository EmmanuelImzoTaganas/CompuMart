module compumart.compumart {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;  // Add this line

    // MongoDB dependencies
    requires org.mongodb.driver.sync.client;
    requires org.mongodb.driver.core;
    requires org.mongodb.bson;

    opens compumart.compumart to javafx.fxml;
    opens compumart.compumart.controller to javafx.fxml;
    opens compumart.compumart.model to javafx.fxml, org.mongodb.bson;
    opens compumart.compumart.service to javafx.fxml, org.mongodb.bson;

    exports compumart.compumart;
    exports compumart.compumart.controller;
    exports compumart.compumart.model;
    exports compumart.compumart.service;
}