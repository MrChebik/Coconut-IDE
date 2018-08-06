package ru.mrchebik.process.save;

import javafx.scene.control.TabPane;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by mrchebik on 9/2/17.
 */
public class SaveTabsProcess extends Thread {
    private TabPane tabPane;

    private SaveTabsProcess(TabPane tabPane) {
        this.tabPane = tabPane;
    }

    public static SaveTabsProcess create(TabPane tabPane) {
        return new SaveTabsProcess(tabPane);
    }

    @Override
    public void run() {
        schedule(() -> SaveTabs.create(tabPane.getTabs()).start());
    }

    public void runSynch() {
        SaveTabs.create(tabPane.getTabs()).run();
    }

    private void schedule(Runnable r) {
        TimerTask task = new TimerTask() {
            public void run() {
                r.run();
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, (long) 5000, (long) 5000);
    }
}
