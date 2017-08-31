package ru.mrchebik.controller;

import javafx.application.Platform;
import ru.mrchebik.controller.javafx.WorkStationController;
import ru.mrchebik.controller.process.EnhancedProcess;
import ru.mrchebik.model.Project;
import ru.mrchebik.view.WorkStation;

import java.nio.file.Path;

/**
 * Created by mrchebik on 09.05.16.
 */
public class Run extends Thread {
    private Path main;

    public Run(Path main) {
        this.main = main;
    }

    @Override
    public void run() {
        Compile compile = new Compile();
        compile.start();
        try {
            compile.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        WorkStationController controller = WorkStation.getFxmlLoader().getController();

        Platform.runLater(() -> {
            if ("".equals(controller.getOutText())) {
                new EnhancedProcess("java",
                        "-cp", Project.getPathOut(),
                        main.toString().substring(Project.getPathOut().length() + 1, main.toString().indexOf('.')).replaceAll("/", ".")).start();
            }
        });
    }
}
