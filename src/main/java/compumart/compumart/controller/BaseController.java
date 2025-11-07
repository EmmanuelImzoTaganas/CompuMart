package compumart.compumart.controller;

import compumart.compumart.CompuMartApplication;
import javafx.scene.control.Alert;

public abstract class BaseController {
    protected CompuMartApplication app;

    public void setApp(CompuMartApplication app) {
        this.app = app;
    }

    public CompuMartApplication getApp() {
        return app;
    }

    protected void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
