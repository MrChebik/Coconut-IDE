package ru.mrchebik.language;

import ru.mrchebik.binaries.BinariesWrapper;
import ru.mrchebik.command.CommandWrapper;
import ru.mrchebik.highlight.pair.PairSymbols;
import ru.mrchebik.highlight.pair.PairSymbolsType;
import ru.mrchebik.language.java.binaries.JavaBinaries;
import ru.mrchebik.language.java.command.JavaCommand;
import ru.mrchebik.language.java.locale.en.EnJavaLocale;
import ru.mrchebik.locale.Locale;
import ru.mrchebik.locale.LocaleType;
import ru.mrchebik.settings.language.LanguageProperty;

import java.util.ArrayList;
import java.util.List;

public class Language {
    public static LanguageType languageType;
    public static List<PairSymbols> pairs;
    public static CommandWrapper command;
    public static BinariesWrapper binaries;

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

            command = new JavaCommand();
            binaries = new JavaBinaries();
        }
    }
}
