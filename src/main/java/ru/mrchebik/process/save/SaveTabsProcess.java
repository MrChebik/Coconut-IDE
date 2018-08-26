package ru.mrchebik.process.save;

import ru.mrchebik.gui.collector.ComponentsCollector;

import java.util.Timer;
import java.util.TimerTask;

public class SaveTabsProcess extends Thread {
    public static void runSynch() {
        new SaveTabs(ComponentsCollector.tabPane.getTabs())
                .run();
    }

    @Override
    public void run() {
        schedule(() -> new SaveTabs(ComponentsCollector.tabPane.getTabs())
                .start());
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
