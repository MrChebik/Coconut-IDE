package ru.mrchebik.process;

import javafx.scene.control.TextArea;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import ru.mrchebik.process.io.InputProcess;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by mrchebik on 8/29/17.
 */
public class ExecutorCommand {
    private Process process;
    private @Setter TextArea outputArea;
    private @Getter @Setter OutputStream outputStream;

    @SneakyThrows(InterruptedException.class)
    public void execute(String command) {
        initializeProcess(command);
        initializeInputStream();
        setOutputStream(process.getOutputStream());
        process.waitFor();
        setOutputStream(null);
    }

    @SneakyThrows(IOException.class)
    private void initializeProcess(String command) {
        String[] divideCommand = command.split(" ");
        ProcessBuilder processBuilder = new ProcessBuilder(divideCommand);
        processBuilder.redirectErrorStream(true);
        process = processBuilder.start();
    }

    private void initializeInputStream() {
        InputStream inputStream = process.getInputStream();
        InputProcess inputProcess = new InputProcess(inputStream, outputArea);
        inputProcess.start();
    }
}
