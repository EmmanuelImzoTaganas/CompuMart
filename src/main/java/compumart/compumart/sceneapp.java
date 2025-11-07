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

public class sceneapp extends Application {

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
        addScene("user-dashboard", "user-main-view.fxml", 1000, 700);
        addScene("product", "product-view.fxml", 1200, 600);
        addScene("cart", "cart-view.fxml", 900, 600);
        addScene("payment", "payment-view.fxml", 950, 650);
        addScene("account", "account-view.fxml", 1000, 700);
        addScene("users", "users-view.fxml", 450, 550);
        addScene("orders", "orders-view.fxml", 450, 550);

        switchTo("home");
        stage.show();
        stage.centerOnScreen();
    }

    private void addScene(String name, String fxml, int width, int height) throws IOException {
        URL fxmlUrl = getClass().getResource("/bcbfixhub/bcbfixhub/" + fxml);
        if (fxmlUrl == null) fxmlUrl = getClass().getResource("/" + fxml);
        if (fxmlUrl == null) throw new IOException("FXML not found: " + fxml);

        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Scene scene = new Scene(loader.load(), width, height);
        scene.setUserData(loader);

        Object controller = loader.getController();
        if (controller instanceof sceneController sc) sc.setApplication(this);

        scenes.put(name, scene);
    }

    public void switchTo(String name) {
        Scene scene = scenes.get(name);
        if (scene == null) {
            try {
                addScene(name, name + "-view.fxml", 800, 600);
                scene = scenes.get(name);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }

    }}