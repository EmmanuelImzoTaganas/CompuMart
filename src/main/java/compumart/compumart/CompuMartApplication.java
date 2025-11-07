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

        // Example usage: preload scenes
        setScene("login", "login-view.fxml");
        setScene("register", "registerview.fxml");

        // Start on login
        switchTo("login");
        mainStage.show();
    }

    private void setScene(String name, String fxml) throws IOException {
        URL fxmlUrl = getClass().getResource("/compumart/compumart/" + fxml);
        if (fxmlUrl == null) fxmlUrl = getClass().getResource("/" + fxml);
        if (fxmlUrl == null) throw new IOException("FXML not found: " + fxml);

        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Scene scene = new Scene(loader.load());

        // Give controller access to the app instance
        BaseController controller = loader.getController();
        if (controller != null) {
            controller.setApp(this);
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
