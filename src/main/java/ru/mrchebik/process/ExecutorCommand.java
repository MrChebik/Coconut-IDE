package ru.mrchebik.process;

import javafx.application.Platform;
import lombok.SneakyThrows;
import ru.mrchebik.gui.collector.ComponentsCollector;
import ru.mrchebik.process.io.ErrorProcess;
import ru.mrchebik.process.io.InputProcess;

import java.io.IOException;
import java.io.OutputStream;

public class ExecutorCommand {
    public static OutputStream outputStream;
    private static Process process;

    @SneakyThrows(InterruptedException.class)
    public static void execute(String command) {
        ComponentsCollector.outputArea.setEditable(true);

        initializeProcess(command);
        initializeErrorStream();
        initializeInputStream();
        outputStream = process.getOutputStream();
        process.waitFor();
        Platform.runLater(() -> ComponentsCollector.outputArea.appendText(InputProcess.line.toString()));
        InputProcess.open = false;
        Platform.runLater(() -> {
            ComponentsCollector.outputArea.appendText((!InputProcess.firstLine || ErrorProcess.wasError ? "\n\n" : "") + "[PROCESS]: " + (ErrorProcess.wasError ? "Failure" : "Success") + "\n");
            ErrorProcess.wasError = false;
        });
        outputStream = null;

        ComponentsCollector.outputArea.setEditable(false);
    }

    private static void initializeErrorStream() {
        ErrorProcess.inputStream = process.getErrorStream();
        ErrorProcess.start();
    }

    private static void initializeInputStream() {
        var inputStream = process.getInputStream();
        var inputProcess = new InputProcess(inputStream);
        inputProcess.start();
    }

    @SneakyThrows(IOException.class)
    private static void initializeProcess(String command) {
        var divideCommand = command.split(" ");
        var processBuilder = new ProcessBuilder(divideCommand);
        process = processBuilder.start();

        Platform.runLater(() -> ComponentsCollector.outputArea.appendText("[COMMAND]: " + command + "\n"));
    }
}
