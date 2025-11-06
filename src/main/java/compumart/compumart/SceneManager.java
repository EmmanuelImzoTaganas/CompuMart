package compumart.compumart;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SceneManager {
    private Stage primaryStage;
    private Map<String, String> fxmlFiles;

    public SceneManager(Stage primaryStage) {
        this.primaryStage = primaryStage;
        initializeFXMLPaths();
    }

    private void initializeFXMLPaths() {
        fxmlFiles = new HashMap<>();
        // Auth screens
        fxmlFiles.put("login", "/compumart/compumart/view/auth/Login.fxml");
        fxmlFiles.put("register", "/compumart/compumart/view/auth/Register.fxml");

        // Customer screens
        fxmlFiles.put("dashboard", "/compumart/compumart/view/customer/Dashboard.fxml");
        fxmlFiles.put("products", "/compumart/compumart/view/customer/Products.fxml");
        fxmlFiles.put("productDetail", "/compumart/compumart/view/customer/ProductDetail.fxml");
        fxmlFiles.put("cart", "/compumart/compumart/view/customer/Cart.fxml");
        fxmlFiles.put("checkout", "/compumart/compumart/view/customer/Checkout.fxml");
        fxmlFiles.put("orderHistory", "/compumart/compumart/view/customer/OrderHistory.fxml");

        // Admin screens
        fxmlFiles.put("adminDashboard", "/compumart/compumart/view/admin/AdminDashboard.fxml");
        fxmlFiles.put("productManagement", "/compumart/compumart/view/admin/ProductManagement.fxml");
        fxmlFiles.put("orderManagement", "/compumart/compumart/view/admin/OrderManagement.fxml");
        fxmlFiles.put("reports", "/compumart/compumart/view/admin/Reports.fxml");
    }

    public void switchTo(String sceneName) {
        try {
            String fxmlPath = fxmlFiles.get(sceneName);
            if (fxmlPath == null) {
                System.err.println("Scene not found: " + sceneName);
                return;
            }

            System.out.println("🔄 Loading scene: " + sceneName + " from: " + fxmlPath);

            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Scene scene = new Scene(root, 1200, 800);

            // Apply CSS if available
            try {
                scene.getStylesheets().add(getClass().getResource("/styles/style.css").toExternalForm());
            } catch (Exception e) {
                System.out.println("ℹ️ No CSS file found, using default styling");
            }

            primaryStage.setScene(scene);
            primaryStage.setTitle(getSceneTitle(sceneName));
            primaryStage.show();

            System.out.println("✅ Switched to: " + sceneName);

        } catch (IOException e) {
            System.err.println("❌ Error loading scene " + sceneName + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String getSceneTitle(String sceneName) {
        switch (sceneName) {
            case "login": return "CompuMart - Login";
            case "register": return "CompuMart - Register";
            case "dashboard": return "CompuMart - Dashboard";
            case "products": return "CompuMart - Products";
            case "cart": return "CompuMart - Shopping Cart";
            case "checkout": return "CompuMart - Checkout";
            case "orderHistory": return "CompuMart - Order History";
            case "adminDashboard": return "CompuMart - Admin Dashboard";
            case "productManagement": return "CompuMart - Product Management";
            case "orderManagement": return "CompuMart - Order Management";
            case "reports": return "CompuMart - Reports";
            default: return "CompuMart";
        }
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }
}