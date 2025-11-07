package compumart.compumart.controller;

import javafx.fxml.FXML;

public class AdminController extends BaseController {

    @FXML
    protected void onLogOut() {
        app.switchTo("login");
    }

    @FXML
    protected void onProducts() {
        app.switchTo("product");
    }
}
