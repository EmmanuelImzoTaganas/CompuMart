package compumart.compumart.controller;

import compumart.compumart.model.User;
import compumart.compumart.model.UserDao;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;

public class RegisterController {

    @FXML
    private TextField registerEmailField;
    @FXML
    private PasswordField registerPasswordField;
    @FXML
    private PasswordField registerConfirmPasswordField;

    private final UserDao userDao = new UserDao();

    @FXML
    private void handleRegister() {
        String email = registerEmailField.getText().trim();
        String password = registerPasswordField.getText().trim();
        String confirm = registerConfirmPasswordField.getText().trim();

        if (email.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all fields.");
            return;
        }

        if (!password.equals(confirm)) {
            showAlert(Alert.AlertType.ERROR, "Error", "Passwords do not match.");
            registerConfirmPasswordField.clear();
            return;
        }

        boolean success = userDao.createUser(email, email, password, ""); // username = email, phone empty
        if (success) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "Registration successful! You can now log in.");
            registerEmailField.clear();
            registerPasswordField.clear();
            registerConfirmPasswordField.clear();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Email already exists. Please use another one.");
        }
    }

    @FXML
    private void onloginpage() {
        try {
            // Load the login FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/compumart/compumart/login.fxml"));
            Parent root = loader.load();

            // Get the current stage
            Stage stage = (Stage) registerEmailField.getScene().getWindow();

            // Set the new scene
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Unable to load login page.");
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