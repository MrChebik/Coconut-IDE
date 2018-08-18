package ru.mrchebik.gui.place.work.event;

import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import ru.mrchebik.process.ExecutorCommand;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

import static ru.mrchebik.gui.key.KeyHelper.isBackSpace;
import static ru.mrchebik.gui.key.KeyHelper.isEnter;

public class InputTextToOutputArea implements EventHandler<KeyEvent> {
    public static String input = "";
    private OutputStream outputStream;

    @Override
    public void handle(KeyEvent event) {
        outputStream = ExecutorCommand.outputStream;
        if (Objects.nonNull(outputStream))
            if (isEnter(event))
                handleEnter();
            else if (isBackSpace(event))
                handleBackSpace();
            else
                addText(event.getText());
    }

    private void addText(String text) {
        input += text;
    }

    private void handleBackSpace() {
        if (input.length() > 0)
            input = input.substring(0, input.length() - 1);
    }

    private void handleEnter() {
        try {
            input += "\n";
            outputStream.write(input.getBytes());
            outputStream.flush();
        } catch (IOException ignored) {
            ExecutorCommand.outputStream = null;
        } finally {
            input = "";
        }
    }
}
