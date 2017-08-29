package ru.mrchebik.controller.process;

import ru.mrchebik.view.Frame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by mrchebik on 8/29/17.
 */
public class EnhancedProcess {
    private String[] commands;

    public EnhancedProcess(String... commands) {
        this.commands = commands;
    }

    public void start() {
        ProcessBuilder processBuilder = new ProcessBuilder(commands);
        processBuilder.redirectErrorStream(true);
        Process process = null;

        try {
            process = processBuilder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (process != null) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            try {
                while ((line = bufferedReader.readLine()) != null) {
                    Frame.out.append(line + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                process.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
