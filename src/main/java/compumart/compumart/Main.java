package compumart.compumart;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {

    private SceneManager sceneManager;

    @Override
    public void start(Stage primaryStage) {
        try {
            System.out.println("Starting CompuMart Application...");

            // Initialize scene manager
            sceneManager = new SceneManager(primaryStage);

            // Start with login screen
            sceneManager.switchTo("login");

            System.out.println("Application started successfully!");

        } catch (Exception e) {
            System.err.println("Error starting application: " + e.getMessage());
            e.printStackTrace();
            showErrorScreen(primaryStage, "Error: " + e.getMessage());
        }
    }

    private void showErrorScreen(Stage primaryStage, String message) {
        Label label = new Label(message);
        label.setStyle("-fx-font-size: 14px; -fx-text-fill: red; -fx-padding: 20;");

        StackPane root = new StackPane(label);
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setTitle("CompuMart - Error");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}