package ru.mrchebik.process.save;

import ru.mrchebik.injection.ComponentsCollector;

import java.util.Timer;
import java.util.TimerTask;

public class SaveTabsProcess extends Thread {
    public static void runSynch() {
        SaveTabs.create(ComponentsCollector.tabPane.getTabs()).run();
    }

    @Override
    public void run() {
        schedule(() -> SaveTabs.create(ComponentsCollector.tabPane.getTabs()).start());
    }

    private void schedule(Runnable r) {
        var task = new TimerTask() {
            public void run() {
                r.run();
            }
        };
        var timer = new Timer();
        timer.schedule(task, (long) 5000, (long) 5000);
    }
}
