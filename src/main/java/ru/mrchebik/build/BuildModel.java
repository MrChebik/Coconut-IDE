package ru.mrchebik.build;

import lombok.AllArgsConstructor;
import ru.mrchebik.helper.FileHelper;
import ru.mrchebik.process.ExecutorCommand;
import ru.mrchebik.process.io.ErrorProcess;

@AllArgsConstructor
public class BuildModel extends FileHelper {
    private ErrorProcess errorProcess;
    private ExecutorCommand executorCommand;
}
