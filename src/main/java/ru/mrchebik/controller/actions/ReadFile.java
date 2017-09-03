package ru.mrchebik.controller.actions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

/**
 * Created by mrchebik on 9/3/17.
 */
public class ReadFile {
    public static String readFile(Path path) {
        String text = null;
        try {
            text = Files.readAllLines(path).stream()
                    .collect(Collectors.joining("\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return text;
    }
}
