package compumart.compumart.controller;

import compumart.compumart.SceneManager;
import compumart.compumart.SessionManager;
import compumart.compumart.model.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class ProductController implements Initializable {

    @FXML private TextField searchField;
    @FXML private ComboBox<String> categoryFilter;
    @FXML private VBox productsContainer;
    @FXML private Button cartButton;
    @FXML private Label welcomeLabel;
    @FXML private HBox userMenu;
    @FXML private Button loginButton;
    @FXML private Button logoutButton;

    private SceneManager sceneManager;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("ProductController initialized");

        // Setup category filter
        categoryFilter.getItems().addAll("All", "Laptops", "Desktops", "Keyboards", "Mice", "Headsets", "Accessories");
        categoryFilter.setValue("All");

        // Update UI based on login status
        updateLoginStatus();
    }

    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    private void updateLoginStatus() {
        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser != null) {
            // User is logged in
            welcomeLabel.setText("Welcome, " + currentUser.getFirstName() + "!");
            loginButton.setVisible(false);
            logoutButton.setVisible(true);
            userMenu.setVisible(true);
        } else {
            // User is not logged in
            welcomeLabel.setText("Welcome to CompuMart!");
            loginButton.setVisible(true);
            logoutButton.setVisible(false);
            userMenu.setVisible(false);
        }
    }

    @FXML
    private void handleSearch() {
        showAlert(Alert.AlertType.INFORMATION, "Search", "Search functionality will be implemented soon!");
    }

    @FXML
    private void handleAddToCart() {
        if (!SessionManager.getInstance().isLoggedIn()) {
            showAlert(Alert.AlertType.WARNING, "Login Required", "Please login to add items to cart.");
            if (sceneManager != null) {
                sceneManager.switchTo("login");
            }
            return;
        }
        showAlert(Alert.AlertType.INFORMATION, "Cart", "Added to cart!");
    }

    @FXML
    private void viewProductDetails() {
        showAlert(Alert.AlertType.INFORMATION, "Product Details", "Product details functionality will be implemented soon!");
    }

    @FXML
    private void goToCart() {
        if (!SessionManager.getInstance().isLoggedIn()) {
            showAlert(Alert.AlertType.WARNING, "Login Required", "Please login to view your cart.");
            if (sceneManager != null) {
                sceneManager.switchTo("login");
            }
            return;
        }
        showAlert(Alert.AlertType.INFORMATION, "Navigation", "Will go to cart screen");
    }

    @FXML
    private void handleLogin() {
        if (sceneManager != null) {
            sceneManager.switchTo("login");
        }
    }

    @FXML
    private void handleLogout() {
        SessionManager.getInstance().logout();
        updateLoginStatus();
        showAlert(Alert.AlertType.INFORMATION, "Logout", "You have been logged out successfully.");
    }

    @FXML
    private void goToOrders() {
        if (!SessionManager.getInstance().isLoggedIn()) {
            showAlert(Alert.AlertType.WARNING, "Login Required", "Please login to view your orders.");
            if (sceneManager != null) {
                sceneManager.switchTo("login");
            }
            return;
        }
        showAlert(Alert.AlertType.INFORMATION, "Navigation", "Will go to orders screen");
    }

    @FXML
    private void goToHome() {
        showAlert(Alert.AlertType.INFORMATION, "Navigation", "Will go to home screen");
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}