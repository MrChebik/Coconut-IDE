package ru.mrchebik.language.java.startup;

import ru.mrchebik.controller.startup.Startup;
import ru.mrchebik.controller.startup.StartupWrapper;
import ru.mrchebik.settings.PropertyCollector;

import java.nio.file.Files;
import java.nio.file.Paths;

public class JavaStartup extends Startup implements StartupWrapper {
    @Override
    public boolean isCorrectHome() {
        return !PropertyCollector.isJDKCorrect()
                && PropertyCollector.getProperty("jdk") == null;
    }

    @Override
    public void newProject() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setupHome(String path) {
        var pathJavac = Paths.get(path, "bin", PropertyCollector.getJavac());
        if (Files.exists(pathJavac))
            PropertyCollector.writeProperty("jdk", path);
    }
}
