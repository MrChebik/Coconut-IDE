package ru.mrchebik.highlight;

import com.github.javaparser.*;
import com.github.javaparser.ast.CompilationUnit;
import org.fxmisc.richtext.CodeArea;
import ru.mrchebik.highlight.pair.PairSymbols;
import ru.mrchebik.language.Language;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class CaretHighlight {
    private static CodeArea codeArea;

    private static int caretPos;
    private static String currText;
    private static int prevPosCaret;
    private static int lastPosCaret;
    private static int lastOtherPos;

    private static List<JavaToken> tokenBrackets;
    private static List<JavaToken> otherTokenBrackets;
    private static JavaToken tokenUser;

    private static Stack<Character> stack;

    static {
        /*lastPosCaret = -1;
        stack = new Stack<>();*/

        clear();
    }

    private static boolean calcHighlight(PairSymbols pair, int caretPos) {
        if (currText.charAt(caretPos) == pair.left) {
            calcNext(pair, caretPos);

            return true;
        } else if (currText.charAt(caretPos) == pair.right) {
            calcPrev(pair, caretPos);

            return true;
        }

        return false;
    }

    private static boolean calcLeft() {
        try {
        for (PairSymbols pair : Language.pairs)
            if (caretPos > 0 &&
                    calcHighlight(pair, caretPos - 1)) {
                caretPos--;
                return true;
            }
        } catch (Exception ignore) {}

        return false;
    }

    private static void calcNext(PairSymbols parentSymbol, int pos) {
        for (int i = pos; i < currText.length(); i++)
            if (computeFragment(currText, i, parentSymbol.left, parentSymbol.right, pos))
                return;
    }

    private static void calcPrev(PairSymbols parentSymbol, int pos) {
        for (int i = pos; i >= 0; i--)
            if (computeFragment(currText, i, parentSymbol.right, parentSymbol.left, pos))
                return;
    }

    private static boolean calcRight() {
        try {
            for (PairSymbols pair : Language.pairs)
                if (calcHighlight(pair, caretPos))
                    return true;
        } catch (Exception ignore) {}

        return false;
    }

    private static void caretNext() {
        if (calcRight())
            return;
        calcLeft();
    }

    private static void caretPrev() {
        if (calcLeft())
            return;
        calcRight();
    }

    private static void clearPrevHighlight() {
        if (isInDuration()) {
            codeArea.setStyleClass(lastPosCaret, lastPosCaret + 1, "empty");
            codeArea.setStyleClass(lastOtherPos, lastOtherPos + 1, "empty");
        }
    }

    public static void compute(CodeArea area) {
        codeArea = area;
        currText = area.getText();

        highlightParents();
    }

    private static boolean computeFragment(String text, int i, char fragment, char mirrFrag, int pos) {
        if (text.charAt(i) == fragment)
            stack.push(fragment);
        else if (text.charAt(i) == mirrFrag) {
            if (stack.peek() == fragment) {
                stack.pop();

                if (stack.size() == 0) {
                    codeArea.setStyleClass(pos, pos + 1, "figure");
                    codeArea.setStyleClass(i, i + 1, "figure");

                    lastPosCaret = pos;
                    lastOtherPos = i;

                    return true;
                }
            } else
                return true;
        }

        return false;
    }

    private static void highlightParents() {
        caretPos = codeArea.getCaretPosition();

        /*System.out.println(caretPos + " // " + prevPosCaret + " -- " + currText.length() + " == " + (prevPosCaret == caretPos || prevPosCaret == caretPos + 1) + " || " + (caretPos + 1 == lastPosCaret || caretPos == lastPosCaret ||
                caretPos + 1 == lastOtherPos || caretPos == lastOtherPos));

        System.out.println(codeArea.getText(codeArea.getCurrentParagraph()));
        System.out.println(codeArea.getCaretColumn() > 0 ? String.format("%" + codeArea.getCaretColumn() + "s", "^") : "^");*/

        try {
            clearHighlight();
            CompilationUnit unit = JavaParser.parse(currText);
            if (unit.getTypes().size() > 0) {
                int paragraph = codeArea.getCurrentParagraph() + 1;
                int column = codeArea.getCaretColumn() + 1;

                TokenRange allRange = unit.getType(0).getTokenRange().get();
                Range other = new Range(new Position(paragraph, column), new Position(paragraph, column));
                allRange.forEach(token -> {
                    Range range = token.getRange().get();
                    System.out.println(token.getKind() + " // " + range.begin.line + " - " + other.begin.line + " // " + (range.begin.column) + " - " + (other.begin.column) + " // " + (range.end.column) + " - " + (other.end.column) + " || " + token.getText());
                    if (range.begin.line == other.begin.line &&
                            (range.begin.column == range.end.column
                                    ?
                                    (range.begin.column == other.begin.column ?
                                            range.end.column == other.end.column
                                            :
                                            (range.begin.column == other.begin.column - 1 &&
                                                    range.end.column == other.end.column - 1))
                                    :
                                    (range.begin.column <= other.begin.column &&
                                            range.end.column >= other.end.column))) {
                        System.out.println("+++" + " // " + token.getKind());
                        if (isSpace(token) || isEnter(token) || isBracket(token)/* token.getKind() == 92 || token.getKind() == 94 || token.getKind() == 96*/) {
                            JavaToken previous = null;
                            JavaToken next = null;
                            if (token.getPreviousToken().isPresent()) {
                                previous = token.getPreviousToken().get();
                            }
                            if (token.getNextToken().isPresent()) {
                                next = token.getNextToken().get();
                            }
                            boolean isCurrent = false;
                            if ((previous != null && (isCurrLine(previous, paragraph) && (isBracket(previous) || isWord(previous)) && !isSpace(previous)))) {
                                System.out.println("PREV");
                                highlight(previous, "figure", false);
                            }
                            if (isBracket(token)) {
                                highlight(token, "figure", false);
                                isCurrent = true;
                            }
                            if ((previous == null || (isBracket(previous) || isWord(previous))) && !isCurrent &&
                                    (next == null || (isCurrLine(next, paragraph) && !isSpace(next) && (isWord(next) || isBracket(next))))) {
                                System.out.println("NEXT");
                                highlight(next, "figure", false);
                            }

                            if (isOpenBracket(token)) {

                            }
                        } else if (isWord(token) || isBracket(token)/*token.getKind() == 95|| token.getKind() == 93 || token.getKind() == 97*/) {
                            highlight(token, "figure", false);
                        }
                    }
                });
            }

            if (tokenBrackets.size() > 1 &&
                    tokenUser != null) {
                highlight(tokenUser, "empty", true);
            }
        } catch (ParseProblemException ignored) {}
        //unit.getType(0).getTokenRange().get().forEach(t -> System.out.println(t.getRange().get().toString()));
            System.out.println("===");

        /*if (!isHighlighted()) {
            clearPrevHighlight();
            caretNext();
        }*/

        /*stack.clear();
        prevPosCaret = caretPos;*/
    }

    private static boolean isBracket(JavaToken token) {
        return token.getKind() == 92 ||  // (
                token.getKind() == 93 || // )
                token.getKind() == 94 || // {
                token.getKind() == 95 || // }
                token.getKind() == 96 || // [
                token.getKind() == 97;   // ]
    }

    private static boolean isOpenBracket(JavaToken token) {
        return token.getKind() == 92 ||  // (
                token.getKind() == 94 || // {
                token.getKind() == 96;   // [
    }

    private static boolean isCloseBracket(JavaToken token) {
        return token.getKind() == 93 ||  // )
                token.getKind() == 94 || // }
                token.getKind() == 96;   // ]
    }

    private static boolean isCurrLine(JavaToken token, int paragraph) {
        return token.getRange().get().begin.line == paragraph;
    }

    private static boolean isWord(JavaToken token) {
        return token.getKind() == 89;
    }

    private static boolean isSpace(JavaToken token) {
        return token.getKind() == 1;
    }

    private static boolean isEnter(JavaToken token) {
        return token.getKind() == 3;
    }

    private static void clear() {
        tokenBrackets = new ArrayList<>();
        otherTokenBrackets = new ArrayList<>();
        tokenUser = null;
    }

    private static void clearHighlight() {
        if (tokenBrackets.size() != 0)
            tokenBrackets.forEach(token -> highlight(token, "empty", true));
        if (otherTokenBrackets.size() != 0)
            otherTokenBrackets.forEach(token -> highlight(token, "empty", true));
        if (tokenUser != null)
            highlight(tokenUser, "empty", true);
        clear();
    }

    private static void highlight(JavaToken token, String style, boolean clear) {
        if (token.getRange().isPresent()) {
            Range range = token.getRange().get();
            if (token.getKind() == 89) {
                if (tokenUser != null) {
                    Range tokenUserRange = tokenUser.getRange().get();
                    codeArea.setStyle(tokenUserRange.begin.line - 1, tokenUserRange.begin.column - 1, tokenUserRange.end.column, Collections.singletonList("empty"));
                }
                if (!clear)
                    tokenUser = token;
                codeArea.setStyle(range.begin.line - 1, range.begin.column - 1, range.end.column, Collections.singletonList(style));
            } else {
                if (!clear)
                    tokenBrackets.add(token);
                codeArea.setStyle(range.begin.line - 1, range.begin.column - 1, range.end.column, Collections.singletonList(style));
            }
        }
    }

    private static boolean isInDuration() {
        return lastPosCaret > -1 && lastPosCaret < currText.length();
    }

    private static boolean isHighlighted() {
        return (codeArea.getStyleOfChar(caretPos).size() > 0 && "figure".equals(codeArea.getStyleOfChar(caretPos).iterator().next())) &&
                (prevPosCaret == caretPos || prevPosCaret == caretPos + 1 || prevPosCaret + 1 == caretPos) &&
                (caretPos + 1 == lastPosCaret || caretPos == lastPosCaret + 1 || caretPos == lastPosCaret ||
                caretPos + 1 == lastOtherPos || caretPos == lastOtherPos || caretPos == lastOtherPos + 1);
    }
}
//{}{{}{{{}}}}