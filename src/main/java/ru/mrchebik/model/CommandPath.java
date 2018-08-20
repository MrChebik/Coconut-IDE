package ru.mrchebik.model;

import java.nio.file.Path;

public class CommandPath {
    public static String command;
    public static Path path;

    public static void init(String command, Path path) {
        CommandPath.command = command;
        CommandPath.path = path;
    }
}
