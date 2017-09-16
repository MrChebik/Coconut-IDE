package ru.mrchebik.process.io;

import javafx.scene.control.TextArea;
import lombok.AllArgsConstructor;
import lombok.Cleanup;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by mrchebik on 8/31/17.
 */
@AllArgsConstructor
public class InputProcess extends Thread {
    private InputStream inputStream;
    private TextArea textArea;

    @Override
    @SneakyThrows(IOException.class)
    public void run() {
        @Cleanup BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = reader.readLine()) != null) {
            appendToOutputArea(line);
        }
    }

    private void appendToOutputArea(String line) {
        String previousLines = textArea.getText();
        String newLines = line + "\n";

        textArea.setText(previousLines + newLines);
    }
}
