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

/**
 * Created by mrchebik on 8/31/17.
 */
public class InputProcess extends Thread {
    private InputStream inputStream;
    private TextArea textArea;
    private @Getter boolean firstLine;

    public InputProcess(InputStream inputStream, TextArea textArea) {
        this.inputStream = inputStream;
        this.textArea = textArea;
    }

    @SneakyThrows(IOException.class)
    public void run() {
        firstLine = true;
        @Cleanup BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = reader.readLine()) != null) {
            if (firstLine) {
                firstLine = false;
            }
            String currLine = line;
            Platform.runLater(() -> textArea.appendText("\n" + currLine));
        }
    }
}
