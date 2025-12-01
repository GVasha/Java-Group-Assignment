package controllers;

import core.AppState;
import core.ScreenManager;

public class BaseController {
    private ScreenManager screenManager;
    private AppState appState = AppState.getInstance();

    public void setScreenManager(ScreenManager screenManager) {
        this.screenManager = screenManager;
    }
}
