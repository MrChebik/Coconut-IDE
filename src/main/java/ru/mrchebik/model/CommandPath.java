package ru.mrchebik.model;

import java.nio.file.Path;

public class CommandPath {
    public String command;
    public Path path;

    private CommandPath() {
    }

    public static CommandPath create() {
        return new CommandPath();
    }
}
