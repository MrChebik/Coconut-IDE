package ru.mrchebik.build;

import ru.mrchebik.command.Command;
import ru.mrchebik.command.CommandType;
import ru.mrchebik.process.ExecutorCommand;
import ru.mrchebik.process.io.ErrorProcess;
import ru.mrchebik.task.TaskHelper;

import java.nio.file.Path;

public class Build extends BuildModel implements BuildWrapper {
    public static Command command;

    public Build(ErrorProcess errorProcess, ExecutorCommand executorCommand) {
        super(errorProcess, executorCommand);
    }

    public void compile() {
        TaskHelper.doTask(CommandType.COMPILE);
    }

    public void run(Path path) {
        var compile = TaskHelper.makeTask(CommandType.COMPILE);
        var run = TaskHelper.makeTask(CommandType.RUN);

        TaskHelper.chain(compile, run);
    }
}
