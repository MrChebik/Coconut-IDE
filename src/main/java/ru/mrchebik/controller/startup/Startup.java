package ru.mrchebik.controller.startup;

public abstract class Startup implements StartupWrapper {
    public void newProject() {
        throw new UnsupportedOperationException();
    }

    public void setupHome(String path) {
        throw new UnsupportedOperationException();
    }

    public boolean isCorrectHome() {
        throw new UnsupportedOperationException();
    }
}
