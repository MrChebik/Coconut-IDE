package ru.mrchebik.language.java.highlight.caret;

import com.github.javaparser.JavaToken;

import java.util.Objects;

class JavaRecursiveSearch extends JavaCaretHighlight {
    static void recursiveSearch(JavaToken token, JavaToken initial, boolean isPrevSearch) {
        if (isConditionToRecursiveForInitial(token, isPrevSearch)) {
            JavaToken nextToken = token.getNextToken().orElse(null);
            if (isSpace(token))
                newEnterRecursive(token, initial, nextToken, isPrevSearch);
            else if (isPrevSearch ?
                    isConditionToRecursiveForPrevSearch(token, initial, nextToken)
                    :
                    isBracket(token)) {
                doHighlight(token, "figure", false);
                doHighlightBracket(token, isPrevSearch);
            }
        }
    }

    private static boolean isConditionToRecursiveForInitial(JavaToken token, boolean isPrevSearch) {
        return Objects.nonNull(token) &&
                isCurrLine(token) &&
                !isEnter(token) &&
                (isPrevSearch || !isCurrWord);
    }

    private static boolean isConditionToRecursiveForPrevSearch(JavaToken token, JavaToken initial, JavaToken nextToken) {
        return Objects.nonNull(nextToken) &&
                isBracketOrWord(token) &&
                (!isWord(token) || (initial.equals(nextToken) || !isSpace(nextToken))) &&
                Objects.requireNonNull(initial.getRange().orElse(null)).begin.column == column &&
                (!isSpace(nextToken) ||
                        (initial.equals(nextToken) || isEnter(initial)));
    }

    private static void newEnterRecursive(JavaToken token, JavaToken initial, JavaToken nextToken, boolean isPrevSearch) {
        var prevToken = token.getPreviousToken().orElse(null);

        var needToken = isPrevSearch ?
                prevToken
                :
                nextToken;
        setTokenToRecursive(needToken, initial, isPrevSearch);
    }

    private static void setTokenToRecursive(JavaToken token1, JavaToken initial, boolean isPrevSearch) {
        if (Objects.nonNull(token1))
            recursiveSearch(token1, initial, isPrevSearch);
    }
}
