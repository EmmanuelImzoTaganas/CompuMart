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
        fxmlFiles.put("login", "/compumart/compumart/view/auth/Login.fxml");
        fxmlFiles.put("register", "/compumart/compumart/view/auth/Register.fxml");
    }

    public void switchTo(String sceneName) {
        try {
            String fxmlPath = fxmlFiles.get(sceneName);
            if (fxmlPath == null) {
                System.err.println("Scene not found: " + sceneName);
                return;
            }

            System.out.println("Loading scene: " + sceneName + " from: " + fxmlPath);

            // Let FXML loader create the controller automatically
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));

            Scene scene = new Scene(root, 900, 700);
            primaryStage.setScene(scene);
            primaryStage.setTitle(getSceneTitle(sceneName));
            primaryStage.show();

            System.out.println("Switched to: " + sceneName);

        } catch (IOException e) {
            System.err.println("Error loading scene " + sceneName + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String getSceneTitle(String sceneName) {
        switch (sceneName) {
            case "login":
                return "CompuMart - Login";
            case "register":
                return "CompuMart - Register";
            default:
                return "CompuMart";
        }
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }
}