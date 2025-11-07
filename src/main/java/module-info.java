module compumart.compumart {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    requires org.mongodb.driver.sync.client;
    requires org.mongodb.bson;
    requires jbcrypt;
    requires org.mongodb.driver.core;
    requires net.synedra.validatorfx;

    opens compumart.compumart to javafx.fxml;
    opens compumart.compumart.controller to javafx.fxml;
    opens compumart.compumart.model to org.mongodb.bson;
    exports compumart.compumart;
    exports compumart.compumart.controller;

}
