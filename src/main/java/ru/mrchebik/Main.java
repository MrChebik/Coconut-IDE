package ru.mrchebik;

import javafx.application.Application;
import ru.mrchebik.arguments.Arguments;
import ru.mrchebik.gui.place.start.StartPlace;
import ru.mrchebik.locale.Locale;

public class Main {
    public static void main(String[] args) {
        Arguments.check(args);
        Locale.init();
        Application.launch(StartPlace.class);
    }
}
