package compumart.compumart.controller;

import compumart.compumart.CompuMartApplication;
import compumart.compumart.model.User;
import javafx.scene.control.Alert;

public abstract class BaseController {
    protected CompuMartApplication app;
    protected User currentUser;

    public void setApp(CompuMartApplication app) {
        this.app = app;
    }

    public CompuMartApplication getApp() {
        return app;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    protected void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Hook called whenever the scene is shown.
     * Controllers can override to refresh UI (e.g., cart count)
     */
    public void onSceneShown() {}
}
