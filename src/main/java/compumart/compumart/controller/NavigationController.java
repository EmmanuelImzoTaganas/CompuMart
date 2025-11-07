package compumart.compumart.controller;

import compumart.compumart.SceneManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;

import java.net.URL;
import java.util.ResourceBundle;



    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    @FXML
    private void switchToRegister() {
        System.out.println("Switching to register screen");
        if (sceneManager != null) {
            sceneManager.switchTo("register");
        } else {
            showAlert("Error", "Scene manager not initialized");
        }
    }

    @FXML
    private void switchToLogin() {
        System.out.println("Switching to login screen");
        if (sceneManager != null) {
            sceneManager.switchTo("login");
        } else {
            showAlert("Error", "Scene manager not initialized");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}