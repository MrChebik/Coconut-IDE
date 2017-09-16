package ru.mrchebik.process.io;

import javafx.scene.control.TextArea;
import lombok.Getter;
import lombok.Setter;

import java.io.InputStream;

/**
 * Created by mrchebik on 9/16/17.
 */
public class ErrorProcess {
    private @Setter @Getter InputStream inputStream;
    private @Setter @Getter TextArea textArea;

    private @Getter @Setter boolean wasError;

    public void start() {
        ErrorThread errorThread = new ErrorThread(this);
        errorThread.start();
    }
}

