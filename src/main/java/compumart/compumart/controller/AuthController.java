package compumart.compumart.controller;

import compumart.compumart.model.User;
import compumart.compumart.service.AuthService;
import compumart.compumart.SceneManager;
import compumart.compumart.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AuthController {

    // Login fields
    @FXML private TextField loginEmailField;
    @FXML private PasswordField loginPasswordField;
    @FXML private Hyperlink registerLink;

    // Register fields
    @FXML private TextField registerFirstNameField;
    @FXML private TextField registerLastNameField;
    @FXML private TextField registerEmailField;
    @FXML private PasswordField registerPasswordField;
    @FXML private PasswordField registerConfirmPasswordField;
    @FXML private Hyperlink loginLink;

    private AuthService authService;
    private Map<String, String> fxmlPaths;
    private SceneManager sceneManager;

    public AuthController() {
        this.authService = new AuthService();
        initializeFXMLPaths();
    }

    private void initializeFXMLPaths() {
        fxmlPaths = new HashMap<>();
        fxmlPaths.put("login", "/compumart/compumart/view/auth/Login.fxml");
        fxmlPaths.put("register", "/compumart/compumart/view/auth/Register.fxml");
        fxmlPaths.put("products", "/compumart/compumart/view/customer/Products.fxml");
        fxmlPaths.put("adminDashboard", "/compumart/compumart/view/admin/AdminDashboard.fxml");
    }

    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    @FXML
    public void initialize() {
        System.out.println("AuthController initialized");

        // Set up navigation handlers
        if (registerLink != null) {
            registerLink.setOnAction(e -> switchToScene("register"));
        }

        if (loginLink != null) {
            loginLink.setOnAction(e -> switchToScene("login"));
        }
    }

    // Login methods
    @FXML
    private void handleLogin() {
        String email = loginEmailField.getText().trim();
        String password = loginPasswordField.getText().trim();

        System.out.println("=== LOGIN ATTEMPT ===");
        System.out.println("Email: " + email);
        System.out.println("Password: " + (password.isEmpty() ? "EMPTY" : "PROVIDED"));

        if (email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter both email and password.");
            return;
        }

        if (!isValidEmail(email)) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid email address.");
            return;
        }

        User user = authService.login(email, password);
        if (user != null) {
            // Login successful - show welcome message
            showAlert(Alert.AlertType.INFORMATION, "Success",
                    "Login successful! Welcome " + user.getFullName() +
                            "\n\nRole: " + (user.isAdmin() ? "Administrator" : "Customer"));

            // Clear fields
            loginEmailField.clear();
            loginPasswordField.clear();

            // Redirect based on user role
            redirectAfterLogin(user);

        } else {
            showAlert(Alert.AlertType.ERROR, "Error",
                    "Invalid email or password. Please try again.");
            loginPasswordField.clear();
            loginPasswordField.requestFocus();
        }
    }

    private void redirectAfterLogin(User user) {
        try {
            if (user.isAdmin()) {
                // Redirect to admin dashboard
                switchToScene("adminDashboard");
                System.out.println("Redirecting to Admin Dashboard for: " + user.getFullName());
            } else {
                // Redirect to customer products page
                switchToScene("products");
                System.out.println("Redirecting to Products page for: " + user.getFullName());
            }
        } catch (Exception e) {
            System.err.println("Error during redirect: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEnterKeyLogin() {
        handleLogin();
    }

    // Register methods
    @FXML
    private void handleRegister() {
        String firstName = registerFirstNameField.getText().trim();
        String lastName = registerLastNameField.getText().trim();
        String email = registerEmailField.getText().trim();
        String password = registerPasswordField.getText();
        String confirmPassword = registerConfirmPasswordField.getText();

        System.out.println("=== REGISTRATION ATTEMPT ===");
        System.out.println("First Name: " + firstName);
        System.out.println("Last Name: " + lastName);
        System.out.println("Email: " + email);
        System.out.println("Password Length: " + password.length());
        System.out.println("Confirm Password Length: " + confirmPassword.length());

        // Validation
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all fields.");
            return;
        }

        if (!isValidEmail(email)) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid email address.");
            return;
        }

        if (password.length() < 6) {
            showAlert(Alert.AlertType.ERROR, "Error", "Password must be at least 6 characters long.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Error", "Passwords do not match.");
            registerConfirmPasswordField.clear();
            registerConfirmPasswordField.requestFocus();
            return;
        }

        // Test database connection first
        authService.testConnection();

        // Check if email exists
        boolean emailExists = authService.emailExists(email);
        System.out.println("Email exists check result: " + emailExists);

        if (emailExists) {
            showAlert(Alert.AlertType.ERROR, "Error", "Email already exists. Please use a different email.");
            return;
        }

        // Create user and register
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);

        System.out.println("Attempting to register user in database...");
        boolean registrationSuccess = authService.register(user, password);
        System.out.println("Registration result: " + registrationSuccess);

        if (registrationSuccess) {
            showAlert(Alert.AlertType.INFORMATION, "Success",
                    "Registration successful! You can now login with your credentials.");

            // Verify the user can login immediately
            System.out.println("Verifying user can login after registration...");
            User verifiedUser = authService.login(email, password);
            if (verifiedUser != null) {
                System.out.println("✅ User verification after registration: SUCCESS");
                System.out.println("Registered user: " + verifiedUser.getFullName());
            } else {
                System.out.println("❌ User verification after registration: FAILED");
                showAlert(Alert.AlertType.WARNING, "Warning",
                        "Registration completed but login verification failed. Please try logging in manually.");
            }

            // Clear all fields
            clearRegisterFields();

            // Switch back to login
            switchToScene("login");

        } else {
            showAlert(Alert.AlertType.ERROR, "Registration Failed",
                    "Registration failed. This could be due to:\n" +
                            "• Database connection issue\n" +
                            "• Email already exists\n" +
                            "• Server problem\n\n" +
                            "Please try again or contact support.");
        }
    }

    @FXML
    private void handleEnterKeyRegister() {
        handleRegister();
    }

    // Test login with demo credentials
    @FXML
    private void testAdminLogin() {
        if (loginEmailField != null && loginPasswordField != null) {
            loginEmailField.setText("admin@compumart.com");
            loginPasswordField.setText("admin123");
            System.out.println("Demo credentials filled. Click Login to test.");
        }
    }

    // Test registration with sample data
    @FXML
    private void testRegistration() {
        if (registerFirstNameField != null && registerEmailField != null) {
            registerFirstNameField.setText("Test");
            registerLastNameField.setText("User");
            registerEmailField.setText("test" + System.currentTimeMillis() + "@test.com");
            registerPasswordField.setText("test123");
            registerConfirmPasswordField.setText("test123");
            System.out.println("Test registration data filled. Click Register to test.");
        }
    }

    // Navigation methods
    @FXML
    private void switchToRegister() {
        System.out.println("Switching to register screen...");
        switchToScene("register");
    }

    @FXML
    private void switchToLogin() {
        System.out.println("Switching to login screen...");
        switchToScene("login");
    }

    private void switchToScene(String sceneName) {
        try {
            String fxmlPath = fxmlPaths.get(sceneName);
            if (fxmlPath == null) {
                System.err.println("Scene not found: " + sceneName);
                showAlert(Alert.AlertType.ERROR, "Error", "Scene not found: " + sceneName);
                return;
            }

            System.out.println("Loading FXML from: " + fxmlPath);

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            // Set scene manager for the controller if it supports it
            Object controller = loader.getController();
            if (controller instanceof AuthController) {
                ((AuthController) controller).setSceneManager(sceneManager);
            } else if (controller instanceof ProductController) {
                ((ProductController) controller).setSceneManager(sceneManager);
            }

            if (root == null) {
                throw new IOException("Failed to load FXML file: " + fxmlPath);
            }

            Stage stage = getCurrentStage();
            if (stage == null) {
                System.err.println("Could not get current stage");
                return;
            }

            Scene scene = new Scene(root, 1200, 800);
            stage.setScene(scene);
            stage.setTitle(getSceneTitle(sceneName));
            stage.show();

            System.out.println("✅ Successfully switched to: " + sceneName);

        } catch (IOException e) {
            System.err.println("❌ Error switching to scene " + sceneName + ": " + e.getMessage());
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error",
                    "Failed to load " + sceneName + " screen: " + e.getMessage());
        }
    }

    private Stage getCurrentStage() {
        if (loginEmailField != null && loginEmailField.getScene() != null) {
            return (Stage) loginEmailField.getScene().getWindow();
        } else if (registerFirstNameField != null && registerFirstNameField.getScene() != null) {
            return (Stage) registerFirstNameField.getScene().getWindow();
        } else if (loginLink != null && loginLink.getScene() != null) {
            return (Stage) loginLink.getScene().getWindow();
        } else if (registerLink != null && registerLink.getScene() != null) {
            return (Stage) registerLink.getScene().getWindow();
        }

        System.err.println("Could not determine current stage - all scene references are null");
        return null;
    }

    private String getSceneTitle(String sceneName) {
        switch (sceneName) {
            case "login": return "CompuMart - Login";
            case "register": return "CompuMart - Register";
            case "products": return "CompuMart - Products";
            case "adminDashboard": return "CompuMart - Admin Dashboard";
            default: return "CompuMart";
        }
    }

    // Helper methods
    private boolean isValidEmail(String email) {
        boolean valid = email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
        System.out.println("Email validation for '" + email + "': " + valid);
        return valid;
    }

    private void clearRegisterFields() {
        if (registerFirstNameField != null) {
            registerFirstNameField.clear();
            System.out.println("Cleared first name field");
        }
        if (registerLastNameField != null) {
            registerLastNameField.clear();
            System.out.println("Cleared last name field");
        }
        if (registerEmailField != null) {
            registerEmailField.clear();
            System.out.println("Cleared email field");
        }
        if (registerPasswordField != null) {
            registerPasswordField.clear();
            System.out.println("Cleared password field");
        }
        if (registerConfirmPasswordField != null) {
            registerConfirmPasswordField.clear();
            System.out.println("Cleared confirm password field");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        System.out.println("Showing alert: " + title + " - " + message);
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Manual database test method (can be called from FXML if needed)
    @FXML
    private void testDatabaseConnection() {
        System.out.println("=== MANUAL DATABASE TEST ===");
        authService.testConnection();

        // Test admin login
        User admin = authService.login("admin@compumart.com", "admin123");
        if (admin != null) {
            showAlert(Alert.AlertType.INFORMATION, "Database Test",
                    "✅ Database connection successful!\n" +
                            "Admin user can login properly.\n" +
                            "Welcome " + admin.getFullName());
        } else {
            showAlert(Alert.AlertType.ERROR, "Database Test",
                    "❌ Database test failed!\n" +
                            "Admin user cannot login.\n" +
                            "Check database connection and initialization.");
        }
    }
}