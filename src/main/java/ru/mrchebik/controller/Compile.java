package ru.mrchebik.controller;

import ru.mrchebik.controller.process.EnhancedProcess;
import ru.mrchebik.model.Project;

/**
 * Created by mrchebik on 08.05.16.
 */
public class Compile {
    public static void start() {
        new EnhancedProcess("javac", Project.getPath() + "/Main.java").start();
    }
}
