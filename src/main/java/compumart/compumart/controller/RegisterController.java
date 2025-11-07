package compumart.compumart.controller;

import compumart.compumart.model.User;
import compumart.compumart.repositories.BaseRepository;
import compumart.compumart.repositories.UserRepository;
import compumart.compumart.utils.PasswordHasher;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import net.synedra.validatorfx.Validator;
import java.time.ZoneId;
import java.util.Date;

public class RegisterController extends BaseController {
    private BaseRepository<User> userRepository;

    @FXML private TextField fNameField;
    @FXML private TextField lNameField; // fixed name
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private TextField phoneField;
    @FXML private TextArea addressField;

    private Validator validator;

    public void initialize() {
        this.userRepository = new UserRepository();
        this.validator = new Validator();

        validator.createCheck()
                .dependsOn("firstName", fNameField.textProperty())
                .withMethod(c -> {
                    String v = c.get("firstName");
                    if (v == null || v.trim().isEmpty()) {
                        c.error("First name is required.");
                    }
                })
                .decorates(fNameField)
                .immediate();

        validator.createCheck()
                .dependsOn("lastName", lNameField.textProperty()) // fixed here
                .withMethod(c -> {
                    String v = c.get("lastName");
                    if (v == null || v.trim().isEmpty()) {
                        c.error("Last name is required.");
                    }
                })
                .decorates(lNameField) // fixed here
                .immediate();

        validator.createCheck()
                .dependsOn("email", emailField.textProperty())
                .withMethod(c -> {
                    String v = c.get("email");
                    if (v == null || v.trim().isEmpty()) {
                        c.error("Email is required.");
                    } else if (!v.matches("\\S+@\\S+\\.\\S+")) {
                        c.error("Email format is invalid.");
                    }
                })
                .decorates(emailField)
                .immediate();

        validator.createCheck()
                .dependsOn("password", passwordField.textProperty())
                .withMethod(c -> {
                    String v = c.get("password");
                    if (v == null || v.isEmpty()) {
                        c.error("Password is required.");
                    }
                })
                .decorates(passwordField)
                .immediate();

        validator.createCheck()
                .dependsOn("confirmPassword", confirmPasswordField.textProperty())
                .dependsOn("password", passwordField.textProperty())
                .withMethod(c -> {
                    String pw = c.get("password");
                    String cpw = c.get("confirmPassword");
                    if (cpw == null || !cpw.equals(pw)) {
                        c.error("Passwords must match.");
                    }
                })
                .decorates(confirmPasswordField)
                .immediate();

        validator.createCheck()
                .dependsOn("phone", phoneField.textProperty())
                .withMethod(c -> {
                    String v = c.get("phone");
                    if (v == null || v.trim().isEmpty()) {
                        c.error("Phone number is required.");
                    } else if (!v.matches("\\d+")) {
                        c.error("Phone number must contain only digits.");
                    }
                })
                .decorates(phoneField)
                .immediate();

        validator.createCheck()
                .dependsOn("address", addressField.textProperty())
                .withMethod(c -> {
                    String v = c.get("address");
                    if (v == null || v.trim().isEmpty()) {
                        c.error("Address is required.");
                    }
                })
                .decorates(addressField)
                .immediate();
    }

    @FXML
    protected void onRegisterUser() {
        validator.validate();
        if (validator.containsErrors()) {
            return;
        }

        String hashedPassword = PasswordHasher.hashPassword(passwordField.getText());

        User user = new User();
        user.setfName(fNameField.getText());
        user.setlName(lNameField.getText());
        user.setEmail(emailField.getText());
        user.setPassword(hashedPassword);
        user.setPhone(phoneField.getText());
        user.setAddress(addressField.getText());
        user.setRole("user"); // default role for new registrations
        user.setCreatedAt(Date.from(java.time.LocalDateTime.now()
                .atZone(ZoneId.systemDefault())
                .toInstant()));

        userRepository.insert(user);
        clearAllFields();
        app.switchTo("login");
    }

    @FXML
    protected void onCancel() {
        clearAllFields();
        app.switchTo("login");
    }

    private void clearAllFields() {
        fNameField.clear();
        lNameField.clear();
        emailField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
        phoneField.clear();
        addressField.clear();
    }
}
