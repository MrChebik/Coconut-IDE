package ru.mrchebik.plugin.debug;

import javafx.application.Platform;
import javafx.scene.control.Label;
import lombok.SneakyThrows;
import ru.mrchebik.model.CustomInteger;
import ru.mrchebik.plugin.Plugin;
import ru.mrchebik.plugin.PluginWrapper;
import ru.mrchebik.plugin.debug.os.OsPluginDebugWrapper;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class PluginDebug extends Plugin implements PluginWrapper {
    protected String measurement;
    protected StringBuilder input;
    protected Process process;
    protected ProcessBuilder processBuilder;
    protected int updateS;
    protected Label label;

    protected ScheduledExecutorService executorService;
    protected OsPluginDebugWrapper osPluginDebugWrapper;

    private CustomInteger character;

    protected PluginDebug(Label label) {
        this.label = label;

        input = new StringBuilder();
        character = new CustomInteger();
        executorService = Executors.newSingleThreadScheduledExecutor();
    }

    @Override
    public void complete() {
        executorService.shutdown();
    }

    protected Runnable compute() {
        return () -> {
            startAndReadOutput();
            writeOutput();
            clear();
        };
    }

    @SneakyThrows(IOException.class)
    protected void startAndReadOutput() {
        process = processBuilder.start();
        var stream = process.getInputStream();

        while (character.setAndGet(stream.read()) != -1)
            input.append((char) character.a);
    }

    private void writeOutput() {
        var text = (String) osPluginDebugWrapper.computeOutput(input);
        var modified = text.replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",");
        Platform.runLater(() -> label.setText(name + ": " + modified + " " + measurement));
    }

    protected void clear() {
        input.setLength(0);
    }

    protected void doesNotSupported() {
        label.setText(name + ": DOES_NOT_SUPPORT " + measurement);
    }

    protected void init() {
        if (osPluginDebugWrapper.isSupported()) {
            processBuilder = new ProcessBuilder()
                    .command(osPluginDebugWrapper.getCommand());

            startService();
        } else
            doesNotSupported();
    }

    @Override
    public void start() {
        if (executorService.isShutdown())
            startService();
    }

    protected void startService() {
        executorService.scheduleAtFixedRate(compute(), 0, updateS, TimeUnit.SECONDS);
    }
}
