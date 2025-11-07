package compumart.compumart.controller;

import compumart.compumart.CompuMartApplication;

public class BaseController {
    protected CompuMartApplication application;

    public CompuMartApplication getApplication() {
        return application;
    }

    public void setApp(CompuMartApplication application) {
        this.application = application;
    }
}
