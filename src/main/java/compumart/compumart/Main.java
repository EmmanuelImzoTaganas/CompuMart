package compumart.compumart;

import compumart.compumart.service.DatabaseService;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            System.out.println("🚀 Starting CompuMart Application...");

            // Set up primary stage
            primaryStage.setTitle("CompuMart - Online Computer Store");
            primaryStage.setMinWidth(900);
            primaryStage.setMinHeight(700);

            // Initialize scene manager
            SceneManager sceneManager = new SceneManager(primaryStage);

            // Start with login screen
            sceneManager.switchTo("login");

            primaryStage.show();
            System.out.println("✅ Application started successfully!");

        } catch (Exception e) {
            System.err.println("❌ Error starting application: " + e.getMessage());
            e.printStackTrace();
            showErrorScreen(primaryStage, "Application Error: " + e.getMessage());
        }
    }

    private void showErrorScreen(Stage primaryStage, String message) {
        javafx.scene.control.Label label = new javafx.scene.control.Label(message);
        label.setStyle("-fx-font-size: 14px; -fx-text-fill: red; -fx-padding: 20; -fx-wrap-text: true;");

        javafx.scene.layout.StackPane root = new javafx.scene.layout.StackPane(label);
        javafx.scene.Scene scene = new javafx.scene.Scene(root, 600, 400);
        primaryStage.setTitle("CompuMart - Error!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}