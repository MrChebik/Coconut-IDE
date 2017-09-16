package ru.mrchebik.process.io;

import lombok.AllArgsConstructor;
import lombok.Cleanup;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by mrchebik on 9/16/17.
 */
@AllArgsConstructor
public class ErrorThread extends Thread {
    private ErrorProcess errorProcess;

    @Override
    @SneakyThrows(IOException.class)
    public void run() {
        errorProcess.setWasError(false);
        @Cleanup BufferedReader reader = new BufferedReader(new InputStreamReader(errorProcess.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            errorProcess.setWasError(true);
            errorProcess.getTextArea().appendText("\n" + line);
        }
    }
}
