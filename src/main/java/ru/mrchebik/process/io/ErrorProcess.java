package ru.mrchebik.process.io;

import javafx.application.Platform;
import lombok.Cleanup;
import lombok.SneakyThrows;
import ru.mrchebik.gui.collector.ComponentsCollector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ErrorProcess {
    public static boolean wasError;
    public static InputStream inputStream;
    public static String line;

    public static void start() {
        clear();

        new ErrorThread().start();
    }

    private static void clear() {
        wasError = false;
    }

    private static class ErrorThread extends Thread {
        @Override
        @SneakyThrows(IOException.class)
        public void run() {
            ErrorProcess.wasError = false;
            @Cleanup BufferedReader reader = new BufferedReader(new InputStreamReader(ErrorProcess.inputStream));
            while ((line = reader.readLine()) != null) {
                ErrorProcess.wasError = true;
                var currLine = line;
                Platform.runLater(() -> ComponentsCollector.outputArea.appendText("\n" + currLine));
            }
        }
    }
}

