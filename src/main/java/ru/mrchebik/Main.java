package ru.mrchebik;

import javafx.application.Application;
import ru.mrchebik.gui.places.start.StartPlace;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by mrchebik on 07.05.16.
 */
public class Main {
    public static void main(String[] args) {
        if ("-exitOnSec".equals(args[0]))
            if (args[1] != null) {
                Timer timer = new Timer();
                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        System.exit(0);
                    }
                };
                timer.schedule(timerTask, Integer.parseInt(args[1]) * 1000);
            }

        Application.launch(StartPlace.class);
    }
}
