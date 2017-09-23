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
    private final Path SETTINGS_PATH = Paths.get(System.getProperty("user.home"), ".coconut-ide");

    private Path pathProperties;
    private Properties properties;

    @SneakyThrows(IOException.class)
    private PropertyCollector() {
        if (!Files.exists(SETTINGS_PATH)) {
            Files.createDirectory(SETTINGS_PATH);
        }
        initializeApplicationProperties();
    }

    public static PropertyCollector create() {
        return new PropertyCollector();
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    @SneakyThrows
    private void initializeApplicationProperties() {
        pathProperties = SETTINGS_PATH.resolve("application.properties");
        if (!Files.exists(pathProperties)) {
            Files.createFile(pathProperties);
        }
        properties = new Properties();
        properties.load(new FileInputStream(new File(String.valueOf(pathProperties.toFile()))));
    }

    @SneakyThrows
    public void writeProperty(String key, String value) {
        properties.setProperty(key, value);

        File file = pathProperties.toFile();
        @Cleanup FileOutputStream fos = new FileOutputStream(file);
        properties.store(fos, "Update");
    }
}
