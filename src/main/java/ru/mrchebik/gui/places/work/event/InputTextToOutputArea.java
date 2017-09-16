package ru.mrchebik.gui.places.work.event;

import javafx.event.EventHandler;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.mrchebik.process.ExecutorCommand;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by mrchebik on 9/15/17.
 */
@RequiredArgsConstructor
public class InputTextToOutputArea implements EventHandler<KeyEvent> {
    private @Setter String input = "";
    @NonNull private ExecutorCommand executorCommand;
    @NonNull private TextArea textArea;

    @Override
    public void handle(KeyEvent event) {
        OutputStream outputStream = executorCommand.getOutputStream();
        if (outputStream != null) {
            if (event.getCode() == KeyCode.ENTER)
                try {
                    input += "\n";
                    outputStream.write(input.getBytes());
                    outputStream.flush();
                } catch (IOException ignored) {
                    executorCommand.setOutputStream(null);
                } finally {
                    input = "";
                }
            else
                if (event.getCode() == KeyCode.BACK_SPACE) {
                    if (input.length() > 0)
                        input = input.substring(0, input.length() - 1);
                } else {
                    input = input.concat(event.getText());
                }
        }
    }
}
