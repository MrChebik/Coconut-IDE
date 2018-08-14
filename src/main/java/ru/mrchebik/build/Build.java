package ru.mrchebik.build;

import ru.mrchebik.command.CommandWrapper;
import ru.mrchebik.process.ExecutorCommand;
import ru.mrchebik.process.io.ErrorProcess;
import ru.mrchebik.task.TaskHelper;

import java.nio.file.Path;

public class Build extends BuildModel implements BuildWrapper {
    public Build(ErrorProcess errorProcess, ExecutorCommand executorCommand, CommandWrapper commandWrapper) {
        super(errorProcess, executorCommand, commandWrapper);
    }

    public void compile() {
        TaskHelper.doTask(command.getCompile());
    }

    public void run(Path path) {
        var compile = TaskHelper.makeTask(command.getCompile());
        var run = TaskHelper.makeTask(command.getRun(path));

        TaskHelper.chain(compile, run);
    }
}
