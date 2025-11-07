package compumart.compumart.controller;

import compumart.compumart.CompuMartApplication;

public abstract class BaseController {
    protected CompuMartApplication app;

    public void setApp(CompuMartApplication app) {
        this.app = app;
    }

    public CompuMartApplication getApp() {
        return app;
    }
}
