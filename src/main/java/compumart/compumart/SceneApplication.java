package compumart.compumart;

import compumart.compumart.controller.*;
import compumart.compumart.model.User;
import compumart.compumart.controller.maincontroller.Product;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class SceneApplication extends Application {

    private Stage mainStage;
    private final Map<String, Scene> scenes = new HashMap<>();
    private final List<Product> cart = new ArrayList<>();
    private User loggedInUser;

    public List<Product> getCart() { return cart; }
    public User getLoggedInUser() { return loggedInUser; }
    public void setLoggedInUser(User user) { this.loggedInUser = user; }

    @Override
    public void start(Stage stage) throws IOException {
        this.mainStage = stage;

        addScene("home", "home-view.fxml", 450, 550);
        addScene("login", "login-view.fxml", 450, 550);
        addScene("register", "register-view.fxml", 450, 550);
        addScene("admin", "admin-view.fxml", 400, 300);
        addScene("user-dashboard", "main-view.fxml", 1000, 700);
        addScene("product", "product-view.fxml", 1200, 600);
        addScene("cart", "cart-view.fxml", 900, 600);
        addScene("payment", "pay-view.fxml", 950, 650);
        addScene("account", "acc-view.fxml", 1000, 700);
        addScene("users", "users-view.fxml", 450, 550);
        addScene("orders", "orders-view.fxml", 450, 550);

        switchTo("home");
        stage.show();
        stage.centerOnScreen();
    }

    private void addScene(String name, String fxml, int width, int height) throws IOException {
        URL fxmlUrl = getClass().getResource("/compumart/compumart/" + fxml);
        if (fxmlUrl == null) fxmlUrl = getClass().getResource("/" + fxml);
        if (fxmlUrl == null) throw new IOException("FXML not found: " + fxml);

        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Scene scene = new Scene(loader.load(), width, height);

        // Store the loader in the scene for later access
        scene.setUserData(loader);

        // CRITICAL: Set the application reference IMMEDIATELY after loading
        Object controller = loader.getController();
        if (controller instanceof SceneController) {
            ((SceneController) controller).SetApplication(this);
        }

        scenes.put(name, scene);
    }

    public void switchTo(String name) {
        Scene scene = scenes.get(name);
        if (scene == null) {
            System.err.println("Scene not found: " + name);
            // Try to load it dynamically
            try {
                addScene(name, name + "-view.fxml", 800, 600);
                scene = scenes.get(name);
            } catch (IOException e) {
                System.err.println("Failed to load scene: " + name);
                e.printStackTrace();
                return;
            }
        }

        mainStage.setScene(scene);
        mainStage.setTitle(capitalize(name));
        mainStage.centerOnScreen();

        // Ensure controller has application reference
        Object controller = getControllerForScene(scene);
        if (controller instanceof SceneController) {
            ((SceneController) controller).SetApplication(this);
        }

        // Refresh data on scene switch
        refreshControllerData(controller);
    }

    private void refreshControllerData(Object controller) {
        Platform.runLater(() -> {
            if (controller instanceof cartcontroller) {
                ((cartcontroller) controller).loadCart();
            } else if (controller instanceof paycontroller) {
                ((paycontroller) controller).loadCartSafely();
            } else if (controller instanceof acccontroller) {
                ((acccontroller) controller).populateCart();
            } else if (controller instanceof maincontroller) {
                ((maincontroller) controller).refreshProducts();
            }
        });
    }

    private Object getControllerForScene(Scene scene) {
        if (scene.getUserData() instanceof FXMLLoader loader) {
            return loader.getController();
        }
        return null;
    }

    private String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    public static void main(String[] args) {
        launch();
    }

    public Object getControllerForScene(String name) {
        Scene scene = scenes.get(name);
        if (scene != null) {
            return getControllerForScene(scene);
        }
        return null;
    }
}