package ru.mrchebik.process.io;

import javafx.application.Platform;
import lombok.Cleanup;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import ru.mrchebik.injection.ComponentsCollector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;

@RequiredArgsConstructor
public class InputProcess extends Thread {
    public static boolean firstLine;
    public static StringBuilder line;
    public static boolean open;

    @NonNull
    private InputStream inputStream;

    @SneakyThrows(IOException.class)
    public void run() {
        @Cleanup BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        initializeTimer();
        open = true;
        firstLine = true;
        int n;
        line = new StringBuilder();
        while ((n = reader.read()) != -1) {
            line.append((char) n);
            if (firstLine) {
                ComponentsCollector.outputArea.appendText("\n");
                firstLine = false;
            }
        }
    }

    private void initializeTimer() {
        var timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (open)
                    print();
            }
        };
        timer.schedule(task, 5, 5);
    }

    private void print() {
        open = false;
        if (!line.toString().isEmpty())
            Platform.runLater(() -> {
                ComponentsCollector.outputArea.appendText(line.toString());
                line = new StringBuilder();
                open = true;
            });
    }
}
