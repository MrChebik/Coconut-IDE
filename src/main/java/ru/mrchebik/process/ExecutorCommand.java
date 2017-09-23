package ru.mrchebik.process;

import javafx.application.Platform;
import javafx.scene.control.TextArea;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import ru.mrchebik.process.io.ErrorProcess;
import ru.mrchebik.process.io.InputProcess;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ExecutorCommand {
    @Setter
    private ErrorProcess errorProcess;
    @Setter
    private TextArea outputArea;
    @Getter @Setter
    private OutputStream outputStream;

    private InputProcess inputProcess;
    private Process process;

    private ExecutorCommand() {
    }

    public static ExecutorCommand create() {
        return new ExecutorCommand();
    }

    @SneakyThrows(InterruptedException.class)
    public void execute(String command) {
        outputArea.setEditable(true);

        initializeProcess(command);
        initializeErrorStream();
        initializeInputStream();
        setOutputStream(process.getOutputStream());
        process.waitFor();
        Platform.runLater(() -> outputArea.appendText(inputProcess.getLine().toString()));
        inputProcess.setOpen(false);
        Platform.runLater(() -> outputArea.appendText((!inputProcess.isFirstLine() || errorProcess.isWasError() ? "\n\n" : "") + "[PROCESS]: " + (errorProcess.isWasError() ? "Failure" : "Success") + "\n"));
        setOutputStream(null);

        outputArea.setEditable(false);
    }

    private void initializeErrorStream() {
        InputStream inputStream = process.getErrorStream();
        errorProcess.setInputStream(inputStream);
        errorProcess.start();
    }

    private void initializeInputStream() {
        InputStream inputStream = process.getInputStream();
        inputProcess = new InputProcess(inputStream, outputArea);
        inputProcess.start();
    }

    @SneakyThrows(IOException.class)
    private void initializeProcess(String command) {
        String[] divideCommand = command.split(" ");
        ProcessBuilder processBuilder = new ProcessBuilder(divideCommand);
        process = processBuilder.start();

        Platform.runLater(() -> outputArea.appendText("[COMMAND]: " + command + "\n"));
    }
}
