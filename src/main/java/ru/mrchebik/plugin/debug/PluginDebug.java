package ru.mrchebik.plugin.debug;

import javafx.application.Platform;
import javafx.scene.control.Label;
import ru.mrchebik.model.CustomInteger;
import ru.mrchebik.plugin.Plugin;
import ru.mrchebik.plugin.debug.os.OsPluginDebugWrapper;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class PluginDebug extends Plugin implements PluginDebugWrapper {
    protected String measurement;
    protected StringBuilder input;
    protected Process process;
    protected int updateMs;
    protected Label label;

    protected OsPluginDebugWrapper osPluginDebugWrapper;

    private CustomInteger character;
    private ProcessBuilder processBuilder;
    private ScheduledExecutorService executorService;

    protected PluginDebug(Label label) {
        this.label = label;
        input = new StringBuilder();
        isStop = false;
        character = new CustomInteger();
        executorService = Executors.newSingleThreadScheduledExecutor();
    }

    @Override
    public void complete() {
        executorService.shutdown();

        super.complete();
    }

    private Runnable compute() {
        return () -> {
            try {
                process = processBuilder.start();
                var stream = process.getInputStream();

                while (character.setAndGet(stream.read()) != -1)
                    input.append((char) character.a);
                var text = osPluginDebugWrapper.computeOutput(input);
                var modified = text.replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",");
                Platform.runLater(() -> label.setText(name + ": " + modified + " " + measurement));
            } catch (IOException e) {
                e.printStackTrace();
            }

            clear();
        };
    }

    private void clear() {
        input.setLength(0);
    }

    private String doesNotSupported() {
        return name + ": DOES_NOT_SUPPORT" + measurement;
    }

    protected void init() {
        if (osPluginDebugWrapper.isSupported()) {
            processBuilder = new ProcessBuilder()
                    .command(osPluginDebugWrapper.getCommand());

            startService();
        } else
            label.setText(doesNotSupported());
    }


    private void startService() {
        executorService.scheduleAtFixedRate(compute(), 0, 2, TimeUnit.SECONDS);
    }
}
