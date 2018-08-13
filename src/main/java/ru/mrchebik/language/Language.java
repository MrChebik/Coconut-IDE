package ru.mrchebik.language;

import ru.mrchebik.highlight.pair.PairSymbols;
import ru.mrchebik.highlight.pair.PairSymbolsType;
import ru.mrchebik.language.java.locale.en.EnJavaLocale;
import ru.mrchebik.locale.Locale;
import ru.mrchebik.locale.LocaleType;
import ru.mrchebik.settings.language.LanguageProperty;

import java.util.ArrayList;
import java.util.List;

public class Language {
    public static LanguageType languageType;
    public static List<PairSymbols> pairs;

    public static void init() {
        var languageCode = LanguageProperty.language;
        languageType = LanguageType.find(languageCode);
        pairs = new ArrayList<>();

        if (languageType.equals(LanguageType.Java)) {
            if (Locale.localeType.equals(LocaleType.English)) {
                EnJavaLocale.init();
            }
            pairs.add(new PairSymbols(PairSymbolsType.FIGURE));
            pairs.add(new PairSymbols(PairSymbolsType.ROUND));
            pairs.add(new PairSymbols(PairSymbolsType.SQUARE));
        }
    }
}
