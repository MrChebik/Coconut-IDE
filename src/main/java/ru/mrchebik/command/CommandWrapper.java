package ru.mrchebik.command;

import java.nio.file.Path;

public interface CommandWrapper {
    String getCompile();
    String getRun(Path path);
}
