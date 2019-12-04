package ru.mrchebik.language.java.highlight.caret;

import com.github.javaparser.JavaToken;
import com.github.javaparser.ParseProblemException;
import com.github.javaparser.ast.CompilationUnit;
import ru.mrchebik.highlight.caret.CaretHighlightFactory;
import ru.mrchebik.highlight.pair.PairSymbols;
import ru.mrchebik.highlight.pair.PairSymbolsType;
import ru.mrchebik.language.java.autocomplete.analyser.JavaAutocompleteAnalyser;
import ru.mrchebik.language.java.highlight.bracket.JavaBracketHighlight;

import java.util.Collections;
import java.util.Objects;

public class JavaCaretHighlight extends CaretHighlightFactory {
    protected static boolean isCurrWord;
    private static JavaBracketHighlight bracketHighlight;
    private static JavaToken tokenUser;

    public JavaCaretHighlight() {
        bracketHighlight = new JavaBracketHighlight();
    }

    protected static void doHighlightBracket(JavaToken token, boolean isPrevSearch) {
        PairSymbols pair = PairSymbolsType.find(token.getText().charAt(0));

        if ((!isPrevSearch || isBracket(token)) && pair != null)
            bracketHighlight.wrapSearchBracket(token, pair);
    }

    private static void currSearch(JavaToken token) {
        if (isBracketOrWord(token))
            doHighlight(token, "figure", false);
        if (!isSpace(token))
            isCurrWord = true;
        doHighlightBracket(token, true);
    }

    protected static boolean isBracket(JavaToken token) {
        return token.getKind() == 92 ||  // (
                token.getKind() == 93 || // )
                token.getKind() == 94 || // {
                token.getKind() == 95 || // }
                token.getKind() == 96 || // [
                token.getKind() == 97;   // ]
    }

    protected static boolean isCurrLine(JavaToken token) {
        return Objects.requireNonNull(token.getRange().orElse(null)).begin.line == paragraph;
    }

    protected static boolean isWord(JavaToken token) {
        return token.getKind() == 89;    // any word except reserved
    }

    protected static boolean isSpace(JavaToken token) {
        return token.getKind() == 1;     // ' '
    }

    protected static boolean isEnter(JavaToken token) {
        return token.getKind() == 3;     // \n
    }

    protected static boolean isBracketOrWord(JavaToken token) {
        return isBracket(token) || isWord(token);
    }

    public static void doHighlight(JavaToken token, String style, boolean other) {
        if (token.getRange().isPresent()) {
            addTokenToStore(token, other);
            highlight(token, style);
        }
    }

    public static void highlight(JavaToken token, String style) {
        if (token.getRange().isPresent()) {
            var range = token.getRange().get();
            codeArea.setStyle(range.begin.line - 1, range.begin.column - 1, range.end.column, Collections.singletonList(style));
        }
    }

    private static void addTokenToStore(JavaToken token, boolean other) {
        if (token.getKind() == 89)
            tokenUser = token;
        else
            bracketHighlight.addTokenToStore(token, other);
    }

    private static boolean isCaretOnToken(JavaToken token) {
        var range = token.getRange().orElse(null);

        return Objects.nonNull(range) &&
                range.begin.line == paragraph &&
                range.begin.column <= column &&
                range.end.column >= column;
    }

    @Override
    protected void clearVariables() {
        bracketHighlight.clearBrackets();
        tokenUser = null;

        isCurrWord = false;

        bracketHighlight.clearVariables();
    }

    @Override
    protected void clearHighlight() {
        try {
            bracketHighlight.clearBracketLists();
            if (Objects.nonNull(tokenUser))
                highlight(tokenUser, "empty");
        } catch (IndexOutOfBoundsException | ParseProblemException ignored) {
        }
    }

    @Override
    protected void highlightNear() {
        try {
            CompilationUnit unit = JavaAutocompleteAnalyser.RAW.parse(currText);
            //var unit = JavaParser.parse(currText);
            if (unit.getTypes().size() > 0) {
                var isWas = false;
                var allRange = unit.getType(0).getTokenRange().orElse(null);

                if (Objects.nonNull(allRange)) {
                    for (JavaToken token : allRange) {
                        if (isCaretOnToken(token)) {
                            isWas = true;

                            var previous = token.getPreviousToken().orElse(null);
                            var next = token.getNextToken().orElse(null);

                            currSearch(token);
                            JavaRecursiveSearch.recursiveSearch(previous, token, true);
                            JavaRecursiveSearch.recursiveSearch(next, token, false);

                            break;
                        }
                    }

                    if (!isWas) {
                        var token = allRange.getEnd();
                        currSearch(token);
                    }
                }
            }
        } catch (ParseProblemException ignored) {
        }
    }
}
