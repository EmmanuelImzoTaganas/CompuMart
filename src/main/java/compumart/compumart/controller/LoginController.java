package compumart.compumart.controller;

import compumart.compumart.model.User;
import compumart.compumart.repositories.UserRepository;
import compumart.compumart.utils.PasswordHasher;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController extends BaseController {
    private UserRepository userRepository;

    @FXML
    private TextField loginEmailField;

    @FXML
    private PasswordField loginPasswordField;

    public void initialize() {
        this.userRepository = new UserRepository();
    }

    private void clearFields() {
        loginEmailField.clear();
        loginPasswordField.clear();
    }

    @FXML
    protected void onLoginHandle() {
        String email = loginEmailField.getText().trim();
        String password = loginPasswordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Missing Fields", "Please enter both email and password.");
            return;
        }

        User user = userRepository.findByEmail(email);

        if (user == null) {
            showAlert(Alert.AlertType.ERROR, "Login Error", "Invalid email or password.");
            return;
        }

        boolean passwordMatch = PasswordHasher.verifyPassword(password, user.getPassword());
        if (!passwordMatch) {
            showAlert(Alert.AlertType.ERROR, "Login Error", "Invalid email or password.");
            return;
        }

        clearFields();

        String role = user.getRole() != null ? user.getRole() : "user";

        // Show welcome message
        showAlert(Alert.AlertType.INFORMATION, "Login Successful",
                "Welcome " + user.getfName() + " " + user.getlName() +
                        (role.equalsIgnoreCase("admin") ? " (Admin)" : "") + "!");


        app.setLoggedInUser(user);
        setCurrentUser(user);
        if ("admin".equalsIgnoreCase(role)) {
            app.switchTo("admin");
        } else {
            app.switchTo("main");
        }


        System.out.println("Logging in user: " + user.getEmail() + ", role: " + user.getRole());
    }


    @FXML
    private void goToRegister() {
        if (app != null) {
            app.switchTo("register");
        }
    }
}
