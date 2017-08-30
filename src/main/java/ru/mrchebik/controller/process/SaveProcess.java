package ru.mrchebik.controller.process;

import javafx.collections.ObservableList;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import ru.mrchebik.controller.Save;
import ru.mrchebik.controller.javafx.WorkStationController;
import ru.mrchebik.view.WorkStation;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by mrchebik on 8/30/17.
 */
public class SaveProcess extends Thread {
    private final Timer t = new Timer();

    public void schedule(Runnable r, long delay, long period) {
        final TimerTask task = new TimerTask() {
            public void run() {
                r.run();
            }
        };
        t.schedule(task, delay, period);
    }

    @Override
    public void run() {
        schedule(() -> {
            WorkStationController controller = WorkStation.getFxmlLoader().getController();
            ObservableList<Tab> tabs = controller.getTabs();

            if (tabs.size() > 0) {
                TextArea textArea = (TextArea) tabs.filtered(e -> e.getText().equals("Main.java")).get(0).getContent();

                Save.start(textArea.getText());
            }
        }, 5000, 5000);
    }
}
