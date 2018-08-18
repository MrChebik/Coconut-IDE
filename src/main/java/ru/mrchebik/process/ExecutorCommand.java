package ru.mrchebik.process;

import javafx.application.Platform;
import lombok.SneakyThrows;
import ru.mrchebik.injection.CollectorComponents;
import ru.mrchebik.process.io.ErrorProcess;
import ru.mrchebik.process.io.InputProcess;

import java.io.IOException;
import java.io.OutputStream;

public class ExecutorCommand {
    private static InputProcess inputProcess;
    public static OutputStream outputStream;
    private static Process process;

    @SneakyThrows(InterruptedException.class)
    public static void execute(String command) {
        CollectorComponents.outputArea.setEditable(true);

        initializeProcess(command);
        initializeErrorStream();
        initializeInputStream();
        outputStream = process.getOutputStream();
        process.waitFor();
        Platform.runLater(() -> CollectorComponents.outputArea.appendText(inputProcess.getLine().toString()));
        inputProcess.setOpen(false);
        Platform.runLater(() -> CollectorComponents.outputArea.appendText((!inputProcess.isFirstLine() || ErrorProcess.wasError ? "\n\n" : "") + "[PROCESS]: " + (ErrorProcess.wasError ? "Failure" : "Success") + "\n"));
        outputStream = null;

        CollectorComponents.outputArea.setEditable(false);
    }

    private static void initializeErrorStream() {
        ErrorProcess.inputStream = process.getErrorStream();
        ErrorProcess.start();
    }

    private static void initializeInputStream() {
        var inputStream = process.getInputStream();
        inputProcess = new InputProcess(inputStream, CollectorComponents.outputArea);
        inputProcess.start();
    }

    @SneakyThrows(IOException.class)
    private static void initializeProcess(String command) {
        var divideCommand = command.split(" ");
        var processBuilder = new ProcessBuilder(divideCommand);
        process = processBuilder.start();

        Platform.runLater(() -> CollectorComponents.outputArea.appendText("[COMMAND]: " + command + "\n"));
    }
}
