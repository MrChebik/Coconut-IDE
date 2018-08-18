package ru.mrchebik.process.io;

import javafx.application.Platform;
import lombok.Cleanup;
import lombok.SneakyThrows;
import ru.mrchebik.injection.CollectorComponents;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ErrorThread extends Thread {
    @Override
    @SneakyThrows(IOException.class)
    public void run() {
        ErrorProcess.wasError = false;
        @Cleanup BufferedReader reader = new BufferedReader(new InputStreamReader(ErrorProcess.inputStream));
        String line;
        while ((line = reader.readLine()) != null) {
            ErrorProcess.wasError = true;
            var currLine = line;
            Platform.runLater(() -> CollectorComponents.outputArea.appendText("\n" + currLine));
        }
    }
}
