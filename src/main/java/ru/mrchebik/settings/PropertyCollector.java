package ru.mrchebik.settings;

import lombok.Cleanup;
import lombok.SneakyThrows;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class PropertyCollector {
    private static final Path SETTINGS_PATH = Paths.get(System.getProperty("user.home"), ".coconut-ide");

    private static Path pathProperties;
    private static Properties properties = new Properties();
    private static String javac;

    @SneakyThrows(IOException.class)
    private PropertyCollector() {
        if (!Files.exists(SETTINGS_PATH)) {
            Files.createDirectory(SETTINGS_PATH);
        }
        initializeApplicationProperties();
    }

    public static String getJavac() {
        if (javac == null) {
            javac = "javac";

            var os = System.getProperty("os.name");
            if (os.contains("Windows")) {
                javac += ".exe";
            }
        }

        return javac;
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    @SneakyThrows
    private void initializeApplicationProperties() {
        pathProperties = SETTINGS_PATH.resolve("application.properties");
        if (!Files.exists(pathProperties)) {
            Files.createFile(pathProperties);
        }
        properties.load(new FileInputStream(new File(String.valueOf(pathProperties.toFile()))));
    }

    public static boolean isJDKCorrect() {
        Path javaHome = Paths.get(System.getProperty("java.home"));
        return Files.exists(javaHome.resolve("bin").resolve(getJavac())) ||
                Files.exists(javaHome.getParent().resolve("bin").resolve(getJavac()));
    }

    @SneakyThrows
    public static void writeProperty(String key, String value) {
        properties.setProperty(key, value);

        File file = pathProperties.toFile();
        @Cleanup FileOutputStream fos = new FileOutputStream(file);
        properties.store(fos, "Update");
    }
}
