package ru.mrchebik.plugin.cpu;

import javafx.application.Platform;
import javafx.scene.control.Label;
import ru.mrchebik.plugin.Plugin;
import ru.mrchebik.plugin.PluginWrapper;

import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.util.concurrent.atomic.AtomicBoolean;

public class PluginCpu extends Plugin implements PluginWrapper {
    public static StringBuilder input;

    public PluginCpu(Label label) {
        pid = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];

        thread = new Thread(compute(label));
        name = "CPU";
        measurement = "%";
        isStop = false;

        input = new StringBuilder();

        isOpenIO = new AtomicBoolean();

        updateMs = 2000;
    }

    @Override
    public void start() {
        thread.start();
    }

    private Runnable compute(Label label) {
        return () -> {
            if ("Linux".contains(System.getProperty("os.name"))) {
                final int[] n = new int[1];
                var builderS = new StringBuilder();
                var builderP = new ProcessBuilder()
                        .command("ps", "-q", pid, "-o", "%cpu");

                while (!isStop) {
                    try {
                        builderS.setLength(0);
                        Process process = builderP.start();

                        InputStream stream = process.getInputStream();
                        isOpenIO.set(true);

                        new Thread(() -> {
                            while (isOpenIO.get()) {
                                try {
                                    while ((n[0] = stream.read()) != -1)
                                        builderS.append((char) n[0]);
                                } catch (IOException ignored) {
                                }
                            }
                        }).start();

                        process.waitFor();

                        isOpenIO.set(false);
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (builderS.length() > 5)
                        Platform.runLater(() -> label.setText(name + ": " + builderS.substring(5, builderS.length() - 1) + measurement));

                    try {
                        Thread.sleep(updateMs);
                    } catch (InterruptedException ignored) {
                    }
                }
            } else
                label.setText(name + ": DOES_NOT_SUPPORT" + measurement);
        };
    }
}
