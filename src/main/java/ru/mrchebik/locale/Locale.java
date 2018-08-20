package ru.mrchebik.locale;

import ru.mrchebik.language.Language;
import ru.mrchebik.locale.en.EnLocale;
import ru.mrchebik.settings.PropertyCollector;

public class Locale {
    public static LocaleType localeType;

    public static String STARTUP;

    public static String NEW_PROJECT;
    public static String NEW_PROJECT_TITLE;

    public static String SETUP_HOME_BUTTON;
    public static String SETUP_HOME_TOOLTIP;
    public static String SETUP_HOME_TITLE;

    public static String CREATE_BUTTON;
    public static String RENAME_BUTTON;

    public static String NAME_LABEL;
    public static String PATH_LABEL;

    public static String COMPILE_BUTTON;
    public static String RUN_BUTTON;

    public static String CREATE_FILE_MENU;
    public static String CREATE_FOLDER_MENU;
    public static String COPY_MENU;
    public static String CUT_MENU;
    public static String PASTE_MENU;
    public static String RENAME_MENU;
    public static String DELETE_MENU;

    public static String CREATE_FILE_TITLE;
    public static String CREATE_FOLDER_TITLE;
    public static String RENAME_FILE_TITLE;
    public static String RENAME_FOLDER_TITLE;

    static {
        var localeCode = PropertyCollector.locale;
        localeType = LocaleType.find(localeCode);

        if (localeType.equals(LocaleType.English))
            EnLocale.init();

        Language.init();
    }
}
