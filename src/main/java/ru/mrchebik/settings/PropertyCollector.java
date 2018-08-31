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
import java.util.Objects;
import java.util.Properties;

public class PropertyCollector {
    /* Properties stored in the config file */
    public static String language;
    public static String locale;
    public static String projects;

    private static Path SETTINGS_PATH;
    private static Path pathProperties;

    private static Properties properties;

    static {
        properties = new Properties();
        initializeSettingsPath();
        initializeApplicationProperties();
        initializeProperties();
    }

    public static void initializeProperties() {
        language = PropertyCollector.initVariable("language", "java");
        locale = PropertyCollector.initVariable("locale", "en");
        projects = PropertyCollector.initVariable("projects", System.getProperty("user.home") + File.separator + "CoconutProjects" + File.separator);
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    @SneakyThrows(IOException.class)
    private static void initializeApplicationProperties() {
        pathProperties = SETTINGS_PATH.resolve("application.properties");
        if (!Files.exists(pathProperties))
            Files.createFile(pathProperties);
        properties.load(new FileInputStream(new File(String.valueOf(pathProperties.toFile()))));
    }

    @SneakyThrows(IOException.class)
    private static void initializeSettingsPath() {
        SETTINGS_PATH = Paths.get(System.getProperty("user.home"), ".coconut-ide");
        if (!Files.exists(SETTINGS_PATH))
            Files.createDirectory(SETTINGS_PATH);
    }

    @SneakyThrows(IOException.class)
    public static void writeProperty(String key, String value) {
        properties.setProperty(key, value);

        var file = pathProperties.toFile();
        @Cleanup FileOutputStream fos = new FileOutputStream(file);
        properties.store(fos, "Update");
    }

    public static String initVariable(String key, String defaultValue) {
        var result = PropertyCollector.getProperty(key);
        if (Objects.isNull(result)) {
            PropertyCollector.writeProperty(key, defaultValue);
            result = defaultValue;
        }

        return result;
    }
}
