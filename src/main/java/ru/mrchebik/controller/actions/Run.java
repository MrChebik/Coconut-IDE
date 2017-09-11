package ru.mrchebik.controller.actions;

import com.google.inject.Inject;
import lombok.SneakyThrows;
import ru.mrchebik.controller.javafx.WorkStationController;
import ru.mrchebik.controller.process.EnhancedProcess;
import ru.mrchebik.model.project.Project;

import java.nio.file.Path;

/**
 * Created by mrchebik on 09.05.16.
 */
public class Run extends Thread {
    private Path main;
    private WorkStationController controller;

    @Inject
    private Project project;

    public Run(Path main, WorkStationController controller) {
        this.main = main;
        this.controller = controller;
    }

    @Override
    @SneakyThrows(InterruptedException.class)
    public void run() {
        Compile compile = new Compile();
        compile.start();
        compile.join();

        if ("".equals(controller.getOutText())) {
            String path = main.toString();
            int indexOfSlashAfterOut = project.getPathSource().toString().length();
            int indexOfDot = path.indexOf('.');

            String pathOfMainClass = path.substring(indexOfSlashAfterOut + 1, indexOfDot);
            String packageOfMainClass = pathOfMainClass.replaceAll("/", ".");

            EnhancedProcess run = new EnhancedProcess("java -cp " + project.getPathOut().toString() + " " + packageOfMainClass);

            run.start();
        }
    }
}
