package ru.mrchebik.plugin.ram;

import javafx.application.Platform;
import javafx.scene.control.Label;
import ru.mrchebik.plugin.Plugin;
import ru.mrchebik.plugin.PluginWrapper;

import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.util.concurrent.atomic.AtomicBoolean;

public class PluginRam extends Plugin implements PluginWrapper {
    public static StringBuilder input;

    public PluginRam(Label label) {
        pid = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];

        thread = new Thread(compute(label));
        name = "RAM";
        measurement = "Mb";
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
                        .command("ps", "-q", pid, "-o", "rss");

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

                    if (builderS.length() > 3) {
                        String text = builderS.substring(builderS.indexOf("\n") + 1, builderS.length() - 1);
                        Platform.runLater(() -> label.setText(name + ": " + computeString(text) + " " + measurement));
                    }

                    try {
                        Thread.sleep(updateMs);
                    } catch (InterruptedException ignored) {
                    }
                }
            } else
                label.setText(name + ": DOES_NOT_SUPPORT" + measurement);
        };
    }

    private String computeString(String text) {
        text = text.length() - 3 > 1 ?
                text.substring(0, text.length() - 3) + "," + text.substring(text.length() - 3)
                :
                text;
        text = text.length() - 7 > 1 ?
                text.substring(0, text.length() - 7) + "," + text.substring(text.length() - 7)
                :
                text;

        return text;
    }
}
