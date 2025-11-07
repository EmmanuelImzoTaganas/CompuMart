package compumart.compumart.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class homecontroller extends sceneController {

    @FXML
    private Button exitButton;

    @FXML
    protected void onLogin() {
        app.switchTo("login");
    }

    @FXML
    protected void onRegister() {
        app.switchTo("register");
    }

    @FXML
    protected void onExit() {
        // Get the stage from the button and close it
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }
}