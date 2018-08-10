package ru.mrchebik.controller.startup;

public abstract class Startup implements StartupWrapper {
    public void newProject() {
        throw new UnsupportedOperationException();
    }

    public void setupHome() {
        throw new UnsupportedOperationException();
    }
}
