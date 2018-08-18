package ru.mrchebik.build;

import ru.mrchebik.command.CommandWrapper;
import ru.mrchebik.task.TaskHelper;

import java.nio.file.Path;

public class Build extends BuildModel implements BuildWrapper {
    public Build(CommandWrapper commandWrapper) {
        super(commandWrapper);
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
