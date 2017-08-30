package ru.mrchebik.controller;

import ru.mrchebik.model.Project;

import java.io.File;
import java.io.IOException;

/**
 * Created by mrchebik on 14.05.16.
 */
public class NewProjectAction {
    public static void start() {
        new File(Project.getPath()).mkdir();
        try {
            new File(Project.getPath() + File.separator + "Main.java").createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
