package ru.mrchebik.actions.autosave.saver.process;

import javafx.scene.control.TabPane;
import lombok.AllArgsConstructor;
import ru.mrchebik.actions.autosave.saver.SaveTabs;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by mrchebik on 9/2/17.
 */
@AllArgsConstructor
public class SaveTabsProcess extends Thread {
    private TabPane tabPane;

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
    public void run() {
        schedule(() -> {
            SaveTabs saver = new SaveTabs(tabPane.getTabs());
            saver.start();
        });
    }
}
