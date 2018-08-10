package ru.mrchebik.language;

import ru.mrchebik.language.java.locale.en.EnJavaLocale;
import ru.mrchebik.locale.Locale;
import ru.mrchebik.locale.LocaleType;
import ru.mrchebik.settings.language.LanguageProperty;

public class Language {
    public static LanguageType languageType;

    public static void init() {
        var languageCode = LanguageProperty.language;
        languageType = LanguageType.find(languageCode);

        if (languageType.equals(LanguageType.Java))
            if (Locale.localeType.equals(LocaleType.English))
                EnJavaLocale.init();
    }
}
