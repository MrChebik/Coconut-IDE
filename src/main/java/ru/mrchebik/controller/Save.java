package ru.mrchebik.controller;

import ru.mrchebik.model.Project;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * Created by mrchebik on 14.05.16.
 */
public class Save {
    public static void start(String text) {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(Project.getPath() + "/Main.java");
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }

        assert writer != null;

        writer.write(text);
        writer.flush();
        writer.close();
    }
}
