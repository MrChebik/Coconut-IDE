package ru.mrchebik.gui.place.start;

import javafx.application.Application;
import javafx.stage.Stage;
import ru.mrchebik.gui.place.ViewHelper;

public class StartPlace extends Application {
    @Override
    public void start(Stage primaryStage) {
        ViewHelper.START.start();
        //ViewHelper.TITLE.start();
    }
}
