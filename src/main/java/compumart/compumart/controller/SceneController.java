package compumart.compumart.controller;

import compumart.compumart.SceneApplication;
import javafx.fxml.FXML;

public class SceneController {
    protected SceneApplication app;

    public void SetApplication(SceneApplication app) {
        this.app = app;
    }

    @FXML
    protected void switchToLogin() {
        app.switchTo("login");
    }

    @FXML
    protected void switchToRegister() {
        app.switchTo("register");
    }


    // Optional helper for dynamic buttons:
    protected void switchTo(String name) {
        app.switchTo(name);
    }
}