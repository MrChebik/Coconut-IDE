package ru.mrchebik.settings.language;

import ru.mrchebik.settings.PropertyCollector;

public class LanguageProperty {
    public static String language;

    static {
        language = PropertyCollector.initVariable("language", "java");
    }
}
