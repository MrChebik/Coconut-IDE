package ru.mrchebik.controller;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Path;

/**
 * Created by mrchebik on 14.05.16.
 */
public class Save extends Thread {
    private Path path;
    private String text;

    public Save(Path path,
                String text) {
        this.path = path;
        this.text = text;
    }

    public void run() {
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
