package ru.mrchebik.run;

import ru.mrchebik.frame.Frame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * Created by mrchebik on 14.05.16.
 */
public class Save implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter("/home/" + System.getProperty("user.name") + "/Coconut-IDE/untitled/Main.java");
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }

        writer.write(Frame.code.getText());
        writer.flush();
        writer.close();
    }

}
