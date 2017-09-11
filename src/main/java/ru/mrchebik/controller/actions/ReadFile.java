package ru.mrchebik.controller.actions;

import lombok.SneakyThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

/**
 * Created by mrchebik on 9/3/17.
 */
public class ReadFile {
    @SneakyThrows(IOException.class)
    public static String readFile(Path path) {
        return Files.readAllLines(path).stream()
                .collect(Collectors.joining("\n"));
    }
}
