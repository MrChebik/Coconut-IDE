package ru.mrchebik.model;

import lombok.Getter;
import lombok.Setter;

import java.nio.file.Path;

public class CommandPath {
    @Getter
    @Setter
    private String command;
    @Getter
    @Setter
    private Path path;

    private CommandPath() {
    }

    public static CommandPath create() {
        return new CommandPath();
    }
}
