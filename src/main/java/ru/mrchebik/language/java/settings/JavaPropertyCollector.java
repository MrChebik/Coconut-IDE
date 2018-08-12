package ru.mrchebik.language.java.settings;

import ru.mrchebik.settings.PropertyCollector;

public class JavaPropertyCollector extends PropertyCollector {
    public static String getProperty(String key) {
        String result = PropertyCollector.getProperty(key);

        return result == null ?
                    System.getProperty("java.home")
                :
                    result;
    }
}
