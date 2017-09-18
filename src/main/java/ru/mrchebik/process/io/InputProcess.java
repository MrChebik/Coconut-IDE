package ru.mrchebik.process.io;

import javafx.application.Platform;
import javafx.scene.control.TextArea;
import lombok.Cleanup;
import lombok.Getter;
import lombok.Setter;
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
    private @Setter boolean open;
    private @Getter StringBuilder line;

    public InputProcess(InputStream inputStream, TextArea textArea) {
        this.inputStream = inputStream;
        this.textArea = textArea;
    }

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
                textArea.appendText("\n");
                firstLine = false;
            }
        }
    }

    private void print() {
        open = false;
        Platform.runLater(() -> {
            textArea.appendText(line.toString());
            line = new StringBuilder();
            open = true;
        });
    }

    private void initializeTimer() {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (open) {
                    print();
                }
            }
        };
        timer.schedule(task, 5, 5);
    }
}
