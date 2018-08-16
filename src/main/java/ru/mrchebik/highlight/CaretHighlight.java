package ru.mrchebik.highlight;

import com.github.javaparser.*;
import com.github.javaparser.ast.CompilationUnit;
import org.fxmisc.richtext.CodeArea;
import ru.mrchebik.highlight.pair.PairSymbols;
import ru.mrchebik.highlight.pair.PairSymbolsType;

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

    private static int paragraph;
    private static int column;
    private static boolean isCurrWord;

    private static List<JavaToken> tokenBrackets;
    private static List<JavaToken> otherTokenBrackets;
    private static JavaToken tokenUser;

    private static List<Stack<String>> stack;

    static {
        /*lastPosCaret = -1;
        stack = new Stack<>();*/

        clear();
    }

    /*private static boolean calcHighlight(PairSymbols pair, int caretPos) {
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
    }*/

    public static void compute(CodeArea area) {
        codeArea = area;
        currText = area.getText();

        highlightParents();
    }

    /*private static boolean computeFragment(String text, int i, char fragment, char mirrFrag, int pos) {
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
    }*/

    private static void highlightParents() {
        caretPos = codeArea.getCaretPosition();

        try {
            clearHighlight();
            CompilationUnit unit = JavaParser.parse(currText);
            if (unit.getTypes().size() > 0) {
                paragraph = codeArea.getCurrentParagraph() + 1;
                column = codeArea.getCaretColumn() + 1;

                boolean isWas = false;

                TokenRange allRange = unit.getType(0).getTokenRange().get();
                Range other = new Range(new Position(paragraph, column), new Position(paragraph, column));
                for (JavaToken token : allRange) {
                    Range range = token.getRange().get();
                    if (range.begin.line == other.begin.line &&
                            (range.begin.column <= other.begin.column &&
                                    range.end.column >= other.end.column)) {
                        isWas = true;

                        JavaToken previous = null;
                        JavaToken next = null;
                        if (token.getPreviousToken().isPresent())
                            previous = token.getPreviousToken().get();
                        if (token.getNextToken().isPresent())
                            next = token.getNextToken().get();

                        if (isBracket(token) || isWord(token))
                            highlight(token, "figure", false, false);
                        if (!isSpace(token))
                            isCurrWord = true;
                        if (isBracket(token))
                            wrapSearchBracket(token, PairSymbolsType.find(token.getText().charAt(0)));
                        prevSearch(previous, token);
                        nextSearch(next, token);

                        break;
                    }
                }


                if (!isWas && paragraph == codeArea.getParagraphs().size() && column - 1 == codeArea.getParagraphLength(paragraph - 1) ) {
                    JavaToken token = allRange.getEnd();

                    if (isBracket(token) || isWord(token))
                        highlight(token, "figure", false, false);
                    if (!isSpace(token))
                        isCurrWord = true;
                    if (isBracket(token))
                        wrapSearchBracket(token, PairSymbolsType.find(token.getText().charAt(0)));
                }
            }

            if (tokenBrackets.size() > 1 &&
                    tokenUser != null) {
                highlight(tokenUser, "empty", true, false);
            }
        } catch (ParseProblemException ignored) {}

        /*if (!isHighlighted()) {
            clearPrevHighlight();
            caretNext();
        }*/

        /*stack.clear();
        prevPosCaret = caretPos;*/
    }

    private static void wrapSearchBracket(JavaToken token, PairSymbols pair) {
        String fragment, mirrFrag;
        int index;
        if (pair.left == token.getText().charAt(0)) {
            fragment = String.valueOf(pair.left);
            mirrFrag = String.valueOf(pair.right);
            index = 1;
        } else {
            fragment = String.valueOf(pair.right);
            mirrFrag = String.valueOf(pair.left);
            index = 0;
        }
        searchBracket(token, fragment, mirrFrag, index);
    }

    private static void searchBracket(JavaToken token, String fragment, String mirrFrag, int index) {
        if (fragment.equals(token.getText()))
            stack.get(index).push(fragment);
        else if (mirrFrag.equals(token.getText())) {
            if (fragment.equals(stack.get(index).peek())) {
                stack.get(index).pop();

                if (stack.get(index).size() == 0) {
                    highlight(token, "figure", false, true);
                    return;
                    /*lastPosCaret = pos;
                    lastOtherPos = i;*/
                }
            } else
                System.out.println("ERROR");
        }

        JavaToken token1 = null;
        if (index == 0 && token.getPreviousToken().isPresent()) {
            token1 = token.getPreviousToken().get();
        } else if (index == 1 && token.getNextToken().isPresent()) {
            token1 = token.getNextToken().get();
        }
        if (token1 != null) {
            searchBracket(token1, fragment, mirrFrag, index);
        }
    }

    private static void prevSearch(JavaToken token, JavaToken initial) {
        if (token != null && isCurrLine(token) && !isEnter(token))
            if (isSpace(token)) {
                if (token.getPreviousToken().isPresent()) {
                    var prevToken = token.getPreviousToken().get();
                    prevSearch(prevToken, initial);
                }
            } else if ((isBracket(token) || isWord(token)) && (initial.equals(token.getNextToken().get()) || !isSpace(token.getNextToken().get())) && initial.getRange().get().begin.column == column) {
                highlight(token, "figure", false, false);
                if (isBracket(token))
                    wrapSearchBracket(token, PairSymbolsType.find(token.getText().charAt(0)));
            }
    }

    private static void nextSearch(JavaToken token, JavaToken initial) {
        if (token != null && isCurrLine(token) && !isEnter(token) && !isCurrWord)
            if (isSpace(token)) {
                if (token.getNextToken().isPresent()) {
                    var nextToken = token.getNextToken().get();
                    nextSearch(nextToken, initial);
                }
            } else if (isBracket(token)) {
                highlight(token, "figure", false, false);
                wrapSearchBracket(token, PairSymbolsType.find(token.getText().charAt(0)));
            }
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

    private static boolean isCurrLine(JavaToken token) {
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

        paragraph = -1;
        column = -1;
        isCurrWord = false;

        stack = new ArrayList<>();
        stack.add(new Stack<>());
        stack.add(new Stack<>());
    }

    private static void clearHighlight() {
        try {
            if (tokenBrackets.size() != 0)
                tokenBrackets.forEach(token -> highlight(token, "empty", true, false));
            if (otherTokenBrackets.size() != 0)
                otherTokenBrackets.forEach(token -> highlight(token, "empty", true, false));
            if (tokenUser != null)
                highlight(tokenUser, "empty", true, false);
        } catch (IndexOutOfBoundsException ignored) {};
        clear();
    }

    private static void highlight(JavaToken token, String style, boolean clear, boolean other) {
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
                if (!clear) {
                    if (!other)
                        tokenBrackets.add(token);
                    else
                        otherTokenBrackets.add(token);
                }
                codeArea.setStyle(range.begin.line - 1, range.begin.column - 1, range.end.column, Collections.singletonList(style));
            }
        }
    }

    /*private static boolean isInDuration() {
        return lastPosCaret > -1 && lastPosCaret < currText.length();
    }*/
}
//{}{{}{{{}}}}