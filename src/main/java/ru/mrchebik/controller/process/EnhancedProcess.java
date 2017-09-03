package ru.mrchebik.controller.process;

import ru.mrchebik.controller.process.io.InputProcess;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

/**
 * Created by mrchebik on 8/29/17.
 */
public class EnhancedProcess {
    private String[] commands;

    private static OutputStream outputStream;

    public EnhancedProcess(String... commands) {
        this.commands = commands;
        outputStream = null;
    }

    public void start() {
        System.out.println(Arrays.toString(commands));

        ProcessBuilder processBuilder = new ProcessBuilder(commands);
        processBuilder.redirectErrorStream(true);
        Process process = null;

        try {
            process = processBuilder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (process != null) {
            InputStream inputStream = process.getInputStream();
            InputProcess inputProcess = new InputProcess(inputStream);
            inputProcess.start();

            outputStream = process.getOutputStream();

            try {
                process.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            outputStream = null;
        }
    }

    public static void setOutputStream() {
        outputStream = null;
    }

    public static OutputStream getOutputStream() {
        return outputStream;
    }
}
