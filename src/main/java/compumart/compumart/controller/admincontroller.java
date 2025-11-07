package compumart.compumart.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public class admincontroller extends SceneController
{
    public VBox register;

    @FXML
    public void onProducts() {
        app.switchTo("product");
    }

    @FXML
    public void onOrder() { app.switchTo("orders"); }

    @FXML
    public void onLogOut() {
        app.switchTo("login");
    }

    @FXML
    public void onUser() {
        app.switchTo("users");
    }

}
