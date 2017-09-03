package ru.mrchebik.controller.actions;

import ru.mrchebik.controller.actions.compile.Compile;
import ru.mrchebik.controller.javafx.WorkStationController;
import ru.mrchebik.controller.process.EnhancedProcess;
import ru.mrchebik.model.Project;

import java.nio.file.Path;

/**
 * Created by mrchebik on 09.05.16.
 */
public class Run extends Thread {
    private Path main;
    private WorkStationController controller;

    public Run(Path main, WorkStationController controller) {
        this.main = main;
        this.controller = controller;
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

        if ("".equals(controller.getOutText())) {
            String path = main.toString();
            int indexOfSlashAfterOut = Project.getPathSource().length();
            int indexOfDot = path.indexOf('.');

            String pathOfMainClass = path.substring(indexOfSlashAfterOut + 1, indexOfDot);
            String packageOfMainClass = pathOfMainClass.replaceAll("/", ".");

            EnhancedProcess run = new EnhancedProcess("java",
                    "-cp", Project.getPathOut(),
                    packageOfMainClass);

            run.start();
        }
    }
}
