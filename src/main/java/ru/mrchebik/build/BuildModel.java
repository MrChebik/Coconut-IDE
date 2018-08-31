package ru.mrchebik.build;

import lombok.AllArgsConstructor;
import ru.mrchebik.command.CommandWrapper;
import ru.mrchebik.util.FileUtil;

@AllArgsConstructor
public class BuildModel extends FileUtil {
    protected CommandWrapper command;
}
