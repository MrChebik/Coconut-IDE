package ru.mrchebik.controller.process;

import ru.mrchebik.controller.process.io.InputProcess;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by mrchebik on 8/29/17.
 */
public class EnhancedProcess {
    private String[] commands;

    private static OutputStream outputStream;

    public EnhancedProcess(String... commands) {
        this.commands = commands;
        EnhancedProcess.outputStream = null;
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
            new InputProcess(process.getInputStream()).start();

            EnhancedProcess.outputStream = process.getOutputStream();

            try {
                process.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            outputStream = null;
        }
    }

    public static void setOutputStream(OutputStream outputStream) {
        EnhancedProcess.outputStream = outputStream;
    }

    public static OutputStream getOutputStream() {
        return outputStream;
    }
}
