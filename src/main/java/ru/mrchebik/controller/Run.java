package ru.mrchebik.controller;

import ru.mrchebik.controller.javafx.WorkStationController;
import ru.mrchebik.controller.process.EnhancedProcess;
import ru.mrchebik.model.Project;
import ru.mrchebik.view.WorkStation;

/**
 * Created by mrchebik on 09.05.16.
 */
public class Run {
    public static void start() {
        Compile.start();

        WorkStationController controller = WorkStation.getFxmlLoader().getController();

        if ("".equals(controller.getOutText())) {
            new EnhancedProcess("java", "-cp", Project.getPath(), "Main").start();
        }
    }
}
