package compumart.compumart.controller;

import compumart.compumart.UserApplication;
import compumart.compumart.model.UserDao;
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

    private final UserDao userDao = new UserDao();

    @FXML
    private void handleLogin(ActionEvent event) {
        String email = loginEmailField.getText().trim();
        String password = loginPasswordField.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error!", "Please fill in all fields.");
            return;
        }
        boolean authenticated = userDao.authenticate(email, password);
        if (authenticated) {
            showAlert(Alert.AlertType.INFORMATION, "Login Successful!", "Welcome back!");

            // Navigate to products page after successful login
            navigateToProducts();

            loginEmailField.clear();
            loginPasswordField.clear();

            try {
                FXMLLoader loader = new FXMLLoader(UserApplication.class.getResource("/compumart/compumart/main-user-view.fxml"));
                Parent root = loader.load();

                // ✅ Get the stage from the current window
                Stage stage = (Stage) loginEmailField.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error!", "Unable to load main user view.");
            }

        } else {
            showAlert(Alert.AlertType.ERROR, "Login Failed.", "Invalid email or password! ");
            loginPasswordField.clear();
        }
    }

    @FXML
    private void onRegisterButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/compumart/compumart/register.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) loginEmailField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error!", "Unable to load registration page.");
        }
    }

    private void navigateToProducts() {
        try {
            // Load the products FXML file - adjust the path based on your project structure
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/compumart/compumart/product.fxml"));
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

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}