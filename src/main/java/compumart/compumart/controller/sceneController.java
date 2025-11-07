package compumart.compumart.controller;

import compumart.compumart.sceneapp;
import javafx.fxml.FXML;

public class sceneController {
    protected sceneapp app;

    public void setApplication(sceneapp app) {
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