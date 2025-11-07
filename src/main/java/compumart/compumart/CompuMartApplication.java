package compumart.compumart;

import compumart.compumart.controller.BaseController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class CompuMartApplication extends Application {

    private Stage mainStage;
    private final Map<String, Scene> scenes = new HashMap<>();

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.mainStage = primaryStage;

        // Load all your scenes once
        loadScene("login", "login-view.fxml");
        loadScene("register", "register-view.fxml"); // fixed typo

        // Show login first
        switchTo("login");
        mainStage.setTitle("CompuMart");
        mainStage.centerOnScreen();
        mainStage.show();
    }

    private void loadScene(String name, String fxmlFile) throws IOException {
        URL fxmlUrl = getClass().getResource("/compumart/compumart/" + fxmlFile);
        if (fxmlUrl == null) {
            fxmlUrl = getClass().getResource("/" + fxmlFile);
        }
        if (fxmlUrl == null) {
            throw new IOException("FXML not found: " + fxmlFile);
        }

        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Scene scene = new Scene(loader.load());

        // Inject the app reference into controller
        Object controller = loader.getController();
        if (controller instanceof BaseController baseController) {
            baseController.setApp(this);
        }

        scenes.put(name, scene);
    }

    public void switchTo(String name) {
        Scene scene = scenes.get(name);
        if (scene != null) {
            mainStage.setScene(scene);
        } else {
            System.err.println("Scene not found: " + name);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
