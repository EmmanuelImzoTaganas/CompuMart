package compumart.compumart.controller;

import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import compumart.compumart.model.User;
import compumart.compumart.service.AuthService;
import compumart.compumart.service.DatabaseService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class AuthController {

    @FXML private TextField loginEmailField;
    @FXML private PasswordField loginPasswordField;

    private Stage primaryStage;
    private AuthService authService;

    public AuthController(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.authService = new AuthService();
    }

    @FXML
    public void initialize() {
        System.out.println("Login screen initialized");
        if (loginEmailField != null) {
            loginEmailField.requestFocus();
        }
    }

    @FXML
    private void handleLogin() {
        String email = loginEmailField.getText().trim();
        String password = loginPasswordField.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter both email and password.");
            return;
        }

        User user = authService.login(email, password);
        if (user != null) {
            showAlert(Alert.AlertType.INFORMATION, "Success",
                    "Login successful! Welcome " + user.getFullName());

            loginEmailField.clear();
            loginPasswordField.clear();

        } else {
            showAlert(Alert.AlertType.ERROR, "Error",
                    "Invalid email or password. Please try again.");
            loginPasswordField.clear();
            loginPasswordField.requestFocus();
        }
    }

    @FXML
    private void showRegister() {
        showAlert(Alert.AlertType.INFORMATION, "Coming Soon",
                "Registration feature will be implemented soon!");
    }

    @FXML
    private void handleEnterKey() {
        handleLogin();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void showLogin() {
        try {
            // Load FXML from the correct package location
            URL fxmlUrl = getClass().getResource("/compumart/compumart/view/auth/Login.fxml");
            if (fxmlUrl == null) {
                throw new IOException("FXML file not found. Looking for: /compumart/compumart/view/auth/Login.fxml");
            }

            System.out.println("FXML URL: " + fxmlUrl);

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            loader.setController(this);
            Parent root = loader.load();

            Scene scene = new Scene(root, 900, 700);
            primaryStage.setScene(scene);
            primaryStage.setTitle("CompuMart - Login");
            primaryStage.show();

            System.out.println("Login screen loaded successfully!");

        } catch (IOException e) {
            System.err.println("Failed to load login screen: " + e.getMessage());
            e.printStackTrace();

            // Show fallback UI
            showFallbackUI();
        }
    }

    private void showFallbackUI() {
        // Create simple UI without FXML
        Label label = new Label(
                "CompuMart Login\n\n" +
                        "Simple UI (FXML not loaded)\n\n" +
                        "Email: admin@compumart.com\n" +
                        "Password: admin123"
        );
        label.setStyle("-fx-font-size: 16px; -fx-alignment: center;");

        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        emailField.setText("admin@compumart.com");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setText("admin123");

        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-background-color: blue; -fx-text-fill: white;");

        loginButton.setOnAction(e -> {
            handleSimpleLogin(emailField.getText(), passwordField.getText());
        });

        VBox root = new VBox(20, label, emailField, passwordField, loginButton);
        root.setStyle("-fx-padding: 40; -fx-alignment: center;");

        Scene scene = new Scene(root, 500, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("CompuMart - Simple UI");
        primaryStage.show();

        System.out.println("Fallback UI loaded");
    }

    private void handleSimpleLogin(String email, String password) {
        User user = authService.login(email, password);
        if (user != null) {
            showAlert(Alert.AlertType.INFORMATION, "Success",
                    "Login successful! Welcome " + user.getFullName());
        } else {
            showAlert(Alert.AlertType.ERROR, "Error",
                    "Invalid email or password. Please try again.");
        }
    }
}