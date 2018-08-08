package ru.mrchebik.process.io;

import javafx.scene.control.TextArea;
import lombok.Getter;
import lombok.Setter;

import java.io.InputStream;

public class ErrorProcess {
    @Getter @Setter
    private InputStream inputStream;
    @Getter @Setter
    private TextArea textArea;
    @Getter @Setter
    private static boolean wasError;

    private ErrorProcess() {
    }

    public static ErrorProcess create() {
        return new ErrorProcess();
    }

    public void start() {
        ErrorThread errorThread = new ErrorThread(this);
        errorThread.start();
    }
}

