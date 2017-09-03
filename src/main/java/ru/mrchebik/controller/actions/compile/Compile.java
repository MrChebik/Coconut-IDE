package ru.mrchebik.controller.actions.compile;

import ru.mrchebik.controller.actions.compile.out.FileSearcher;
import ru.mrchebik.controller.process.EnhancedProcess;
import ru.mrchebik.model.Project;

/**
 * Created by mrchebik on 08.05.16.
 */
public class Compile extends Thread {
    @Override
    public void run() {
        FileSearcher fileSearcher = new FileSearcher(".java");
        fileSearcher.startSeacrh();

        EnhancedProcess compile = new EnhancedProcess("javac",
                "-d", Project.getPathOut(),
                "@" + Project.getPathOutListStructure());
        compile.start();
    }
}
