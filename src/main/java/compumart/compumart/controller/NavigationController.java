package compumart.compumart.controller;

import compumart.compumart.SceneManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;

import java.net.URL;
import java.util.ResourceBundle;

public class NavigationController implements Initializable {

    private SceneManager sceneManager;

    @FXML private Hyperlink registerLink;
    @FXML private Hyperlink loginLink;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Assign simple click actions
        if (registerLink != null) registerLink.setOnAction(e -> navigate("register"));
        if (loginLink != null) loginLink.setOnAction(e -> navigate("login"));
    }

    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    // Navigates to a specific scene name using the SceneManager.Shows an alert if the SceneManager hasn't been set.

    private void navigate(String targetScene) {
        if (sceneManager == null) {
            showError("Scene Manager not initialized. Cannot switch to " + targetScene + ".");
            return;
        }
        sceneManager.switchTo(targetScene);
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Navigation Error!");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
