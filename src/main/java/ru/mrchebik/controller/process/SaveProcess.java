package ru.mrchebik.controller.process;

import javafx.scene.control.TextArea;
import ru.mrchebik.controller.Save;
import ru.mrchebik.controller.javafx.WorkStationController;
import ru.mrchebik.view.WorkStation;

import java.nio.file.Path;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by mrchebik on 8/30/17.
 */
public class SaveProcess extends Thread {
    private final Timer t = new Timer();

    private WorkStationController controller;

    public void schedule(Runnable r) {
        final TimerTask task = new TimerTask() {
            public void run() {
                r.run();
            }
        };
        t.schedule(task, (long) 5000, (long) 5000);
    }

    @Override
    public void run() {
        controller = WorkStation.getFxmlLoader().getController();

        schedule(() -> controller.getTabs().forEach(tab -> {
            TextArea area = (TextArea) tab.getContent();

            new Save((Path) tab.getUserData(), area.getText()).start();
        }));
    }
}
