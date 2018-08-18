package ru.mrchebik.language.java.settings;

import ru.mrchebik.settings.PropertyCollector;

import java.nio.file.Files;
import java.nio.file.Paths;

public class JavaPropertyCollector extends PropertyCollector {
    public static String javac;

    static {
        javac = "javac";

        var os = System.getProperty("os.name");
        if (os.contains("Windows"))
            javac += ".exe";
    }

    public static String getProperty(String key) {
        String result = PropertyCollector.getProperty(key);

        return result == null ?
                System.getProperty("java.home")
                :
                result;
    }

    public static boolean isJdkCorrect() {
        var javaHomeProperty = System.getProperty("java.home");
        var javaHomePath = Paths.get(javaHomeProperty);

        var endPath = Paths.get("bin", javac);
        var javacPath = javaHomePath.resolve(endPath);
        var javacParentPath = javaHomePath.getParent().resolve(endPath);

        return Files.exists(javacPath) ||
                Files.exists(javacParentPath);
    }
}
