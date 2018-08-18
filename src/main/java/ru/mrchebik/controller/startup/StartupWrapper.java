package ru.mrchebik.controller.startup;

public interface StartupWrapper {
    boolean isCorrectHome();

    void newProject();

    void setupHome(String path);
}
