package ru.mrchebik.controller;

import ru.mrchebik.view.Frame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * Created by mrchebik on 14.05.16.
 */
public class Save {
    public static void start() {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter("/home/" + System.getProperty("user.name") + "/Coconut-IDE/untitled/Main.java");
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }

        assert writer != null;
        writer.write(Frame.code.getText());
        writer.flush();
        writer.close();
    }
}
