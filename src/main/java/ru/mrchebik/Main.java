package ru.mrchebik;

import javafx.application.Application;
import ru.mrchebik.gui.place.start.StartPlace;

import java.util.Timer;
import java.util.TimerTask;

public class Main {
    public static void main(String[] args) {
        ifTravisCi(args);

        Application.launch(StartPlace.class);
    }

    private static void ifTravisCi(String[] args) {
        if (args.length > 1 &&
                "-exitOnSec".equals(args[0])) {
            var timer = new Timer();
            var task = new TimerTask() {
                @Override
                public void run() {
                    System.exit(0);
                }
            };
            var seconds = Integer.parseInt(args[1]) * 1000;
            timer.schedule(task, seconds);
        }
    }
}
