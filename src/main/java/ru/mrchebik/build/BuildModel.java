package ru.mrchebik.build;

import lombok.AllArgsConstructor;
import ru.mrchebik.command.CommandWrapper;
import ru.mrchebik.helper.FileAction;

@AllArgsConstructor
public class BuildModel extends FileAction {
    protected CommandWrapper command;
}
