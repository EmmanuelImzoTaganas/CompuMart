package compumart.compumart.controller;

import compumart.compumart.SessionManager;
import compumart.compumart.model.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminController implements Initializable {

    @FXML private Label welcomeLabel;
    @FXML private Label statsLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("AdminController initialized");

        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser != null && currentUser.isAdmin()) {
            welcomeLabel.setText("Welcome, Admin " + currentUser.getFullName() + "!");
            statsLabel.setText("Admin Dashboard - Manage products, orders, and view reports");
        } else {
            welcomeLabel.setText("Unauthorized Access!");
            statsLabel.setText("You do not have admin privileges.");
        }
    }

    @FXML
    private void manageProducts() {
        showAlert(Alert.AlertType.INFORMATION, "Manage Products", "Product management functionality coming soon!");
    }

    @FXML
    private void manageOrders() {
        showAlert(Alert.AlertType.INFORMATION, "Manage Orders", "Order management functionality coming soon!");
    }

    @FXML
    private void viewReports() {
        showAlert(Alert.AlertType.INFORMATION, "View Reports", "Reports functionality coming soon!");
    }

    @FXML
    private void logout() {
        SessionManager.getInstance().logout();
        showAlert(Alert.AlertType.INFORMATION, "Logout", "You have been logged out.");
        // Should redirect to login screen
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}