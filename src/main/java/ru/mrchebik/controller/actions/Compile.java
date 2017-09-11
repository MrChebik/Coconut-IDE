package ru.mrchebik.controller.actions;

import com.google.inject.Inject;
import ru.mrchebik.controller.process.EnhancedProcess;
import ru.mrchebik.model.project.Project;

/**
 * Created by mrchebik on 08.05.16.
 */
public class Compile extends Thread {
    @Inject
    private Project project;

    @Override
    public void run() {
        String listOfFiles = project.getStructure(".java");

        EnhancedProcess compile = new EnhancedProcess("javac -d " + project.getPathOut().toString() + " " + listOfFiles);
        compile.start();
    }
}
