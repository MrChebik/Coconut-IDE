package ru.mrchebik.build;

import ru.mrchebik.command.CommandWrapper;
import ru.mrchebik.task.TaskAction;

import java.nio.file.Path;

public class Build extends BuildModel implements BuildWrapper {
    public Build(CommandWrapper commandWrapper) {
        super(commandWrapper);
    }

    public void compile() {
        TaskAction.doTask(command.getCompile());
    }

    public void run(Path path) {
        var compile = TaskAction.makeTask(command.getCompile());
        var run = TaskAction.makeTask(command.getRun(path));

        TaskAction.chain(compile, run);
    }
}
