package ru.mrchebik.controller;

import ru.mrchebik.controller.process.EnhancedProcess;
import ru.mrchebik.view.Frame;

import java.io.*;

/**
 * Created by mrchebik on 08.05.16.
 */
public class Compile {
    public static void start() {
        Frame.out.setText("");

        new File("/home/" + System.getProperty("user.name") + "/Coconut-IDE").mkdir();
        new File("/home/" + System.getProperty("user.name") + "/Coconut-IDE/untitled").mkdir();
        try {
            new File("/home/" + System.getProperty("user.name") + "/Coconut-IDE/untitled/Main.java").createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Save.start();

        new EnhancedProcess("javac", "/home/" + System.getProperty("user.name") + "/Coconut-IDE/untitled/Main.java").start();
    }
}
