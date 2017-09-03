package ru.mrchebik.controller.actions.autosave.saver;

import javafx.collections.ObservableList;
import javafx.scene.control.Tab;
import ru.mrchebik.controller.actions.autosave.Autosave;
import ru.mrchebik.controller.javafx.WorkStationController;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by mrchebik on 9/2/17.
 */
public class SaveTabsProcess extends Autosave {
    private WorkStationController controller;

    public SaveTabsProcess(WorkStationController controller) {
        this.controller = controller;
    }

    private void schedule(Runnable r) {
        Timer timer = new Timer();

        TimerTask task = new TimerTask() {
            public void run() {
                r.run();
            }
        };

        timer.schedule(task, (long) 5000, (long) 5000);
    }

    @Override
    public void save() {
        schedule(() -> {
            ObservableList<Tab> tabs = controller.getTabs();

            Autosave saver = new SaveTabs(tabs);
            saver.start();
        });
    }
}
