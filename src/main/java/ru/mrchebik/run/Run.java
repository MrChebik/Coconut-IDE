package ru.mrchebik.run;

import ru.mrchebik.frame.Frame;
import ru.mrchebik.run.Compile;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by mrchebik on 09.05.16.
 */
public class Run implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        Compile.start();

        try {
            run();
        } catch (IOException e1) {
            Frame.out.append(e1.getLocalizedMessage());
            e1.printStackTrace();
        }
    }

    private void run() throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder("java", "-cp", "/home/" + System.getProperty("user.name") + "/Coconut-IDE/untitled", "Main");
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();

        InputStream inputStream = process.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        String line;
        while ((line = bufferedReader.readLine()) != null) {
            Frame.out.append(line);
        }

        try {
            System.out.println(process.waitFor());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
