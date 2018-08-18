package ru.mrchebik.settings.locale;

import ru.mrchebik.settings.PropertyCollector;

public class LocaleProperty {
    public static String locale;

    static {
        locale = PropertyCollector.initVariable("locale", "en");
    }
}
