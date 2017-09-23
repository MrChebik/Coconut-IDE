package ru.mrchebik;

import javafx.application.Application;
import ru.mrchebik.gui.place.start.StartPlace;
import ru.mrchebik.settings.PropertyCollector;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by mrchebik on 07.05.16.
 */
public class Main {
    public static void main(String[] args) {
        if (args.length > 1 && "-exitOnSec".equals(args[0])) {
            Timer timer = new Timer();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    System.exit(0);
                }
            };
            timer.schedule(timerTask, Integer.parseInt(args[1]) * 1000);
        }

        PropertyCollector collector = PropertyCollector.create();
        //collector.writeProject("TES1T", "/home/mrchebik/123");

        Application.launch(StartPlace.class);
    }
}
