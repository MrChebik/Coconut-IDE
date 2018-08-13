package ru.mrchebik.locale;

import ru.mrchebik.language.Language;
import ru.mrchebik.locale.en.EnLocale;
import ru.mrchebik.settings.locale.LocaleProperty;

public class Locale {
    public static LocaleType localeType;

    public static String STARTUP;
    public static String NEW_PROJECT;
    public static String SETUP_HOME_TOOLTIP;
    public static String SETUP_HOME_TITLE;
    public static String NEW_PROJECT_TITLE;

    static {
        var localeCode = LocaleProperty.locale;
        localeType = LocaleType.find(localeCode);

        if (localeType.equals(LocaleType.English))
            EnLocale.init();

        Language.init();
    }
}
