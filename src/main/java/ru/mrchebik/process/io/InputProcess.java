package ru.mrchebik.process.io;

import javafx.application.Platform;
import javafx.scene.control.TextArea;
import lombok.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;

@RequiredArgsConstructor
public class InputProcess extends Thread {
    @Getter
    private boolean firstLine;
    @Getter
    private StringBuilder line;
    @Setter
    private boolean open;

    @NonNull
    private InputStream inputStream;
    @NonNull
    private TextArea textArea;

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
                textArea.appendText(line.toString());
                line = new StringBuilder();
                open = true;
            });
    }
}
