package ru.mrchebik.build;

import ru.mrchebik.command.Command;
import ru.mrchebik.command.CommandType;
import ru.mrchebik.process.io.ErrorProcess;
import ru.mrchebik.process.io.ExecutorCommand;
import ru.mrchebik.project.Project;
import ru.mrchebik.task.TaskHelper;

import java.nio.file.Path;

public class Build extends BuildModel implements BuildWrapper {
    public static Command command;

    public Build(Project project, ErrorProcess errorProcess, ExecutorCommand executorCommand) {
        super(project, errorProcess, executorCommand);
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
