package ru.mrchebik.settings.language;

import ru.mrchebik.settings.PropertyCollector;

public class LanguageProperty {
    public static String language;

    static {
        language = PropertyCollector.getProperty("language");
        if (language == null) {
            PropertyCollector.writeProperty("language", "java");
            language = "java";
        }
    }
}
