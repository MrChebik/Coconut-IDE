package ru.mrchebik.controller.startup;

public abstract class Startup implements StartupWrapper {
    public boolean isCorrectHome() {
        throw new UnsupportedOperationException();
    }

    public void newProject() {
        throw new UnsupportedOperationException();
    }
    public void setupHome(String path) {
        throw new UnsupportedOperationException();
    }
}
