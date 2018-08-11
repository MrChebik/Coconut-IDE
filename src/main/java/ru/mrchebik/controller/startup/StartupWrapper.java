package ru.mrchebik.controller.startup;

public interface StartupWrapper {
    void newProject();
    void setupHome(String path);
    boolean isCorrectHome();
}
