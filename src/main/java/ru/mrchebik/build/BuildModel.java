package ru.mrchebik.build;

import lombok.AllArgsConstructor;
import ru.mrchebik.command.CommandWrapper;
import ru.mrchebik.helper.FileHelper;

@AllArgsConstructor
public class BuildModel extends FileHelper {
    protected CommandWrapper command;
}
