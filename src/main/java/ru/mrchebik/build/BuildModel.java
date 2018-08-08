package ru.mrchebik.build;

import lombok.AllArgsConstructor;
import ru.mrchebik.helper.FileHelper;
import ru.mrchebik.process.io.ErrorProcess;
import ru.mrchebik.process.io.ExecutorCommand;
import ru.mrchebik.project.Project;

@AllArgsConstructor
public class BuildModel extends FileHelper {
    private Project project;

    private ErrorProcess errorProcess;
    private ExecutorCommand executorCommand;
}
