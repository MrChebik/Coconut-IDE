package ru.mrchebik.process.io;

import javafx.application.Platform;
import lombok.AllArgsConstructor;
import lombok.Cleanup;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
            String currLine = line;
            Platform.runLater(() -> errorProcess.getTextArea().appendText("\n" + currLine));
        }
    }
}
