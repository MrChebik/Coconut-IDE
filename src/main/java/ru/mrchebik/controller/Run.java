package ru.mrchebik.controller;

import ru.mrchebik.controller.javafx.WorkStationController;
import ru.mrchebik.controller.process.EnhancedProcess;
import ru.mrchebik.model.Project;
import ru.mrchebik.view.WorkStation;

import java.io.File;
import java.nio.file.Path;

/**
 * Created by mrchebik on 09.05.16.
 */
public class Run {
    public static void start(Path main) {
        Compile.start();

        WorkStationController controller = WorkStation.getFxmlLoader().getController();

        if ("".equals(controller.getOutText())) {
            new EnhancedProcess("java",
                    "-cp", Project.getPathOut().substring(0, Project.getPathOut().lastIndexOf(File.separator)) + File.pathSeparator + "out" + File.pathSeparator + "**" + File.separator + "*.class",
                    main.toString().substring(Project.getPathOut().length(), main.toString().indexOf('.')).replaceAll("/", ".")).start();
        }
    }
}
