package ru.mrchebik.process;

import javafx.application.Platform;
import javafx.scene.control.TextArea;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import ru.mrchebik.process.io.ErrorProcess;
import ru.mrchebik.process.io.InputProcess;

import java.io.IOException;
import java.io.OutputStream;

public class ExecutorCommand {
    @Setter
    private static ErrorProcess errorProcess;
    @Setter
    private static TextArea outputArea;
    @Getter @Setter
    private static OutputStream outputStream;

    private static InputProcess inputProcess;
    private static Process process;

    public static ExecutorCommand create() {
        return new ExecutorCommand();
    }

    @SneakyThrows(InterruptedException.class)
    public static void execute(String command) {
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

    private static void initializeErrorStream() {
        var inputStream = process.getErrorStream();
        errorProcess.setInputStream(inputStream);
        errorProcess.start();
    }

    private static void initializeInputStream() {
        var inputStream = process.getInputStream();
        inputProcess = new InputProcess(inputStream, outputArea);
        inputProcess.start();
    }

    @SneakyThrows(IOException.class)
    private static void initializeProcess(String command) {
        var divideCommand = command.split(" ");
        var processBuilder = new ProcessBuilder(divideCommand);
        process = processBuilder.start();

        Platform.runLater(() -> outputArea.appendText("[COMMAND]: " + command + "\n"));
    }
}
