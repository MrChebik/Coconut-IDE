package ru.mrchebik.language;

import ru.mrchebik.autocomplete.analyser.AutocompleteAnalyser;
import ru.mrchebik.binaries.BinariesWrapper;
import ru.mrchebik.command.CommandWrapper;
import ru.mrchebik.highlight.caret.CaretHighlightFactory;
import ru.mrchebik.highlight.pair.PairSymbols;
import ru.mrchebik.highlight.pair.PairSymbolsType;
import ru.mrchebik.highlight.syntax.SyntaxWrapper;
import ru.mrchebik.language.java.autocomplete.analyser.JavaAutocompleteAnalyser;
import ru.mrchebik.language.java.binaries.JavaBinaries;
import ru.mrchebik.language.java.command.JavaCommand;
import ru.mrchebik.language.java.highlight.caret.JavaCaretHighlight;
import ru.mrchebik.language.java.highlight.syntax.switcher.compiler.JavaCompilerSyntax;
import ru.mrchebik.language.java.highlight.syntax.switcher.symbolsolver.JavaSymbolSolverSyntax;
import ru.mrchebik.language.java.settings.JavaPropertyCollector;
import ru.mrchebik.locale.Locale;
import ru.mrchebik.settings.PropertyCollector;

import java.util.ArrayList;
import java.util.List;

public class Language {
    public static LanguageType languageType;
    public static List<PairSymbols> pairs;
    public static CommandWrapper command;
    public static BinariesWrapper binaries;
    public static CaretHighlightFactory caretHighlight;
    public static AutocompleteAnalyser autocompleteAnalyser;

    public static void init() {
        var languageCode = PropertyCollector.language;
        languageType = LanguageType.find(languageCode);
        pairs = new ArrayList<>();

        if (languageType.equals(LanguageType.Java)) {
            Locale.initPart(languageType.toString());

            pairs.add(new PairSymbols(PairSymbolsType.FIGURE));
            pairs.add(new PairSymbols(PairSymbolsType.ROUND));
            pairs.add(new PairSymbols(PairSymbolsType.SQUARE));

            command = new JavaCommand();
            binaries = new JavaBinaries();
            caretHighlight = new JavaCaretHighlight();

            autocompleteAnalyser = new JavaAutocompleteAnalyser();
        }
    }

    public static SyntaxWrapper initSyntax() {
        if (languageType.equals(LanguageType.Java))
            return JavaPropertyCollector.isJdkCorrect() ?
                    new JavaCompilerSyntax()
                    :
                    new JavaSymbolSolverSyntax();

        throw new UnsupportedOperationException();
    }
}
