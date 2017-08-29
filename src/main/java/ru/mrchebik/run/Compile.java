package ru.mrchebik.run;

import ru.mrchebik.frame.Frame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

/**
 * Created by mrchebik on 08.05.16.
 */
public class Compile implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        start();
    }

    static void start() {
        Frame.out.setText("");

        checknameectoryForIDE();
        checknameectoryForProjects();
        checkFileForProjects();

        Frame.save.doClick();

        try {
            compile();
        } catch (IOException e1) {
            Frame.out.setText(e1.getLocalizedMessage());
            e1.printStackTrace();
        }
    }

    private static void compile() throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder("javac", "/home/" + System.getProperty("user.name") + "/Coconut-IDE/untitled/Main.java");
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

    private static void checkFileForProjects() {
        if (!new File("/home/" + System.getProperty("user.name") + "/Coconut-IDE/untitled/Main.java").exists()) {
            try {
                new File("/home/" + System.getProperty("user.name") + "/Coconut-IDE/untitled/Main.java").createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void checknameectoryForProjects() {
        if (!new File("/home/" + System.getProperty("user.name") + "/Coconut-IDE/untitled").exists()) {
            new File("/home/" + System.getProperty("user.name") + "/Coconut-IDE/untitled").mkdir();
        }
    }

    private static void checknameectoryForIDE() {
        if (!new File("/home/" + System.getProperty("user.name") + "/Coconut-IDE").exists()) {
            new File("/home/" + System.getProperty("user.name") + "/Coconut-IDE").mkdir();
        }
    }

}
