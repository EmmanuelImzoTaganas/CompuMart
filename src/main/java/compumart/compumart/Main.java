package compumart.compumart;

import compumart.compumart.controller.AuthController;
import compumart.compumart.service.DatabaseService;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        System.out.println("Starting CompuMart Application...");

        // Initialize database
        try {
            DatabaseService.getInstance().initializeDatabase();
            System.out.println("Database initialized successfully");
        } catch (Exception e) {
            System.err.println("Failed to initialize database: " + e.getMessage());
            e.printStackTrace();
        }

        // Show login screen
        AuthController authController = new AuthController(primaryStage);
        authController.showLogin();
    }

    public static void main(String[] args) {
        System.out.println("Launching JavaFX application...");
        launch(args);
    }
}