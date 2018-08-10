package ru.mrchebik.gui.place.work.event;

import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.mrchebik.process.ExecutorCommand;

import java.io.IOException;
import java.io.OutputStream;

import static ru.mrchebik.gui.key.KeyHelper.isBackSpace;
import static ru.mrchebik.gui.key.KeyHelper.isEnter;

@RequiredArgsConstructor
public class InputTextToOutputArea implements EventHandler<KeyEvent> {
    @Setter
    private String input = "";
    @NonNull
    private ExecutorCommand executorCommand;

    private OutputStream outputStream;

    @Override
    public void handle(KeyEvent event) {
        outputStream = executorCommand.getOutputStream();
        if (outputStream != null) {
            if (isEnter(event)) {
                handleEnter();
            } else if (isBackSpace(event)) {
                handleBackSpace();
            } else {
                addText(event.getText());
            }
        }
    }

    private void addText(String text) {
        input += text;
    }

    private void handleBackSpace() {
        if (input.length() > 0) {
            input = input.substring(0, input.length() - 1);
        }
    }

    private void handleEnter() {
        try {
            input += "\n";
            outputStream.write(input.getBytes());
            outputStream.flush();
        } catch (IOException ignored) {
            executorCommand.setOutputStream(null);
        } finally {
            input = "";
        }
    }
}
