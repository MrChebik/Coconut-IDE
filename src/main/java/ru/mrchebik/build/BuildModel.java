package ru.mrchebik.build;

import lombok.AllArgsConstructor;
import ru.mrchebik.algorithm.AlgorithmFile;
import ru.mrchebik.command.CommandWrapper;

@AllArgsConstructor
public class BuildModel extends AlgorithmFile {
    protected CommandWrapper command;
}
