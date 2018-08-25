package ru.mrchebik.plugin.debug.cpu;

import javafx.application.Platform;
import javafx.scene.control.Label;
import ru.mrchebik.plugin.PluginWrapper;
import ru.mrchebik.plugin.debug.PluginDebug;
import ru.mrchebik.plugin.debug.cpu.linux.LinuxPluginDebugCpu;
import ru.mrchebik.plugin.debug.os.OsPluginDebug;

import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

public class PluginDebugCpu extends PluginDebug implements PluginWrapper {
    private boolean isNew;
    private UtimeTotalTime store;

    public PluginDebugCpu(Label label) {
        super(label);

        name = "CPU";
        measurement = "%";
        updateS = 2;
        osPluginDebugWrapper = OsPluginDebug.getOs(
                new LinuxPluginDebugCpu());
        isNew = true;

        init();
    }

    @Override
    protected Runnable compute() {
        return (() -> {
            super.startAndReadOutput();

            var part = (UtimeTotalTime) osPluginDebugWrapper.computeOutput(input);

            if (!isNew) {
                String result = doCalculations(part);
                Platform.runLater(() -> label.setText(name + ": " + result + " " + measurement));
            }

            store = part;
            isNew = !isNew;

            super.clear();
        });
    }

    @Override
    protected void init() {
        if (osPluginDebugWrapper.isSupported()) {
            processBuilder = new ProcessBuilder()
                    .command(osPluginDebugWrapper.getCommand());

            startService();
        } else
            super.doesNotSupported();
    }

    @Override
    protected void startService() {
        executorService.scheduleAtFixedRate(compute(), 0, updateS, TimeUnit.SECONDS);
    }

    private String doCalculations(UtimeTotalTime part) {
        double result = calculateFormula(part);
        DecimalFormat dec = new DecimalFormat();
        dec.setMaximumFractionDigits(1);
        return dec.format(result);
    }

    private double calculateFormula(UtimeTotalTime time) {
        return 100 *
                (double) (time.utime - store.utime) /
                (double) (time.total_time - store.total_time);
    }
}
