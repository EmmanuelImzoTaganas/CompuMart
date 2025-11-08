package compumart.compumart;

import compumart.compumart.controller.BaseController;
import compumart.compumart.repositories.CartRepository;
import compumart.compumart.model.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class CompuMartApplication extends Application {

    private User loggedInUser;
    private Stage mainStage;
    private final Map<String, Scene> scenes = new HashMap<>();

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.mainStage = primaryStage;

        // Load all scenes
        loadScene("login", "login-view.fxml");
        loadScene("register", "register-view.fxml");
        loadScene("admin", "admin-view.fxml");
        loadScene("main", "main-view.fxml");
        loadScene("product", "product-view.fxml");
        loadScene("cart", "cart-view.fxml");

        // Add close handler
        primaryStage.setOnCloseRequest(event -> {
            if (loggedInUser != null) {
                if (loggedInUser.getCart() != null) {
                    new CartRepository().saveOrUpdate(loggedInUser.getCart());
                    System.out.println("Saved cart for user: " + loggedInUser.getEmail() + " on exit.");
                } else {
                    System.out.println("No cart to save for user: " + loggedInUser.getEmail());
                }
            } else {
                System.out.println("No user logged in on exit — skipping cart save.");
            }
            // Note: If you want to **prevent** closing in some cases, you can call event.consume()
        });

        // Show login first
        switchTo("login");
        mainStage.setTitle("CompuMart");
        mainStage.centerOnScreen();
        mainStage.show();
    }



    private void loadScene(String name, String fxmlFile) throws IOException {
        URL fxmlUrl = getClass().getResource("/compumart/compumart/" + fxmlFile);
        if (fxmlUrl == null) fxmlUrl = getClass().getResource("/" + fxmlFile);
        if (fxmlUrl == null) throw new IOException("FXML not found: " + fxmlFile);

        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Scene scene = new Scene(loader.load());
        scene.setUserData(loader); // store loader for later

        // Load CSS if exists
        String cssPath = "/compumart/compumart/cssfiles/" + name + ".css";
        URL cssUrl = getClass().getResource(cssPath);
        if (cssUrl != null) scene.getStylesheets().add(cssUrl.toExternalForm());

        // Inject app reference into controller
        Object controller = loader.getController();
        if (controller instanceof BaseController baseController) {
            baseController.setApp(this);
        }

        scenes.put(name, scene);
    }

    public void switchTo(String name) {
        Scene scene = scenes.get(name);
        if (scene == null) {
            System.err.println("Scene not found: " + name);
            return;
        }

        mainStage.setScene(scene);
        mainStage.sizeToScene();
        mainStage.centerOnScreen();

        FXMLLoader loader = (FXMLLoader) scene.getUserData();
        Object controller = loader.getController();
        if (controller instanceof BaseController baseController) {
            baseController.setCurrentUser(loggedInUser);
            baseController.onSceneShown(); // refresh UI
        }

        System.out.println("Switching to scene: " + name);
    }

    public void setLoggedInUser(User user) {
        this.loggedInUser = user;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
