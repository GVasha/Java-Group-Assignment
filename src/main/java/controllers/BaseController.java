package controllers;

import core.AppState;
import core.ScreenManager;

public class BaseController {
    protected ScreenManager screenManager;
    protected AppState appState = AppState.getInstance();

    public void setScreenManager(ScreenManager screenManager) {
        this.screenManager = screenManager;
    }
}
