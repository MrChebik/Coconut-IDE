package ru.mrchebik.controller;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Path;

/**
 * Created by mrchebik on 14.05.16.
 */
public class Save {
    public static void start(Path path, String text) {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(path.toFile());
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }

        assert writer != null;

        writer.write(text);
        writer.flush();
        writer.close();
    }
}
