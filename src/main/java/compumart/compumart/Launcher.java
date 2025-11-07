package compumart.compumart;  // FIXED PACKAGE

import javafx.application.Application;

public class Launcher {
    public static void main(String[] args) {
        Application.launch(UserApplication.class, args); // Launch Main.class instead of HelloApplication
    }
}