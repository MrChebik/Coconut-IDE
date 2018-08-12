package ru.mrchebik.ci;

import java.util.Timer;
import java.util.TimerTask;

public class ContinuousIntegration {
    public static void init() {
        var timer = new Timer();
        var task = new TimerTask() {
            @Override
            public void run() {
                System.exit(0);
            }
        };
        timer.schedule(task, 3000);
    }
}
