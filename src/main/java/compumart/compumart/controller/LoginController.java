package compumart.compumart.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.io.IOException;

public class LoginController {

    @FXML
    private TextField loginEmailField;
    @FXML
    private PasswordField loginPasswordField;

    private final UserDAO userDao = new UserDAO();

    // Hardcoded admin credentials
    private static final String ADMIN_EMAIL = "admin@123.com";
    private static final String ADMIN_PASSWORD = "12345";

    @FXML
    private void handleLogin(ActionEvent event) {
        String email = loginEmailField.getText().trim();
        String password = loginPasswordField.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error!", "Please fill in all fields.");
            return;
        }

        // Check if it's the hardcoded admin
        if (ADMIN_EMAIL.equals(email) && ADMIN_PASSWORD.equals(password)) {
            showAlert(Alert.AlertType.INFORMATION, "Admin Login Successful", "Welcome Admin!");

            // Navigate to admin products page
            navigateToAdminProducts();

            loginEmailField.clear();
            loginPasswordField.clear();
            return;
        }

        // Regular user authentication
        boolean authenticated = userDao.authenticate(email, password);
        if (authenticated) {
            showAlert(Alert.AlertType.INFORMATION, "Login Successful!", "Welcome back!");

            // Navigate to regular user products page
            navigateToProducts();

            loginEmailField.clear();
            loginPasswordField.clear();

        } else {
            showAlert(Alert.AlertType.ERROR, "Login Failed.", "Invalid email or password! ");
            loginPasswordField.clear();
        }
    }

    private void navigateToAdminProducts() {
        try {
            // Load the admin product management FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/compumart/compumart/admin-view.fxml"));
            Parent root = loader.load();

            // Get the current stage
            Stage stage = (Stage) loginEmailField.getScene().getWindow();

            // Set the new scene
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("CompuMart - Admin Panel");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Unable to load admin panel.");
        }
    }

    private void navigateToProducts() {
        try {
            // Load the regular user products FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/compumart/compumart/main-view.fxml"));
            Parent root = loader.load();

            // Get the current stage
            Stage stage = (Stage) loginEmailField.getScene().getWindow();

            // Set the new scene
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("CompuMart - Products");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error!", "Unable to load products page.");
        }
    }

    @FXML
    private void onRegisterButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/compumart/compumart/register-view.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) loginEmailField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error!", "Unable to load registration page.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}