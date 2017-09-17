package ru.mrchebik.process.io;

import javafx.application.Platform;
import javafx.scene.control.TextArea;
import lombok.Cleanup;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by mrchebik on 8/31/17.
 */
public class InputProcess extends Thread {
    private InputStream inputStream;
    private TextArea textArea;
    private @Getter boolean firstLine;
    private boolean open;
    private @Getter StringBuilder line;

    public InputProcess(InputStream inputStream, TextArea textArea) {
        this.inputStream = inputStream;
        this.textArea = textArea;
    }

    @SneakyThrows(IOException.class)
    public void run() {
        @Cleanup BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        initializeTimer();
        open = false;
        firstLine = true;
        int n;
        line = new StringBuilder();
        while ((n = reader.read()) != -1) {
            line.append((char) n);
            if (open) {
                open = false;
                if (firstLine) {
                    textArea.appendText("\n");
                    firstLine = false;
                }
                Platform.runLater(() -> {
                    textArea.appendText(line.toString());
                    line = new StringBuilder();
                });
            }
        }
    }

    private void initializeTimer() {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                open = true;
            }
        };
        timer.schedule(task, 100, 100);
    }
}
