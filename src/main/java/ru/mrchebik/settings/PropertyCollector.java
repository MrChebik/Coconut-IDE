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

    private Properties lastProjects;
    private Path pathLastProjects;
    private Path pathProperties;
    private Properties properties;

    @SneakyThrows(IOException.class)
    private PropertyCollector() {
        if (!Files.exists(SETTINGS_PATH)) {
            Files.createDirectory(SETTINGS_PATH);
        }
        initializeApplicationProperties();
        initializeLastProjectsProperties();
    }

    public static PropertyCollector create() {
        return new PropertyCollector();
    }

    public String getProject(String key) {
        return lastProjects.getProperty(key);
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

    public void initializeLastProject() {

    }

    @SneakyThrows
    private void initializeLastProjectsProperties() {
        pathLastProjects = SETTINGS_PATH.resolve("lastProjects.properties");
        if (!Files.exists(pathLastProjects)) {
            Files.createFile(pathLastProjects);
        }
        lastProjects = new Properties();
        lastProjects.load(new FileInputStream(new File(String.valueOf(pathLastProjects.toFile()))));
    }

    @SneakyThrows
    public void writeProject(String key, String value) {
        lastProjects.setProperty(key, value);

        File file = pathLastProjects.toFile();
        @Cleanup FileOutputStream fos = new FileOutputStream(file);
        lastProjects.store(fos, "Update");
    }

    @SneakyThrows
    public void writeProperty(String key, String value) {
        properties.setProperty(key, value);

        File file = pathProperties.toFile();
        @Cleanup FileOutputStream fos = new FileOutputStream(file);
        properties.store(fos, "Update");
    }
}
