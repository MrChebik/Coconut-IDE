package ru.mrchebik.controller;

import ru.mrchebik.controller.process.EnhancedProcess;
import ru.mrchebik.view.Frame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by mrchebik on 09.05.16.
 */
public class Run {
    public static void start() {
        Compile.start();

        if ("".equals(Frame.out.getText())) {
            new EnhancedProcess("java", "-cp", "/home/" + System.getProperty("user.name") + "/Coconut-IDE/untitled", "Main").start();
        }
    }
}
