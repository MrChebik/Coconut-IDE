package ru.mrchebik;

import javafx.application.Application;
import ru.mrchebik.arguments.Arguments;
import ru.mrchebik.gui.place.start.StartPlace;

public class Main {
    public static void main(String[] args) {
        Arguments.check(args);

        Application.launch(StartPlace.class);
    }
}
