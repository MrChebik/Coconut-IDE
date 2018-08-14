package ru.mrchebik.highlight.pair;

import org.fxmisc.richtext.CodeArea;
import ru.mrchebik.language.Language;

import java.util.Stack;

public class ParentsHighlight {
    private static CodeArea codeArea;

    private static int caretPos;
    private static String currText;
    private static int lastPosCaret;
    private static int lastOtherPos;

    private static Stack<Character> stack;

    static {
        lastPosCaret = -1;
        stack = new Stack<>();
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
        for (PairSymbols pair : Language.pairs)
            if (caretPos > 0 &&
                    calcHighlight(pair, caretPos - 1)) {
                caretPos--;
                return true;
            }

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
        for (PairSymbols pair : Language.pairs)
            if (caretPos < currText.length() &&
                    calcHighlight(pair, caretPos))
                return true;

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

        clearPrevHighlight();
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

                    stack.clear();

                    return true;
                }
            } else
                return true;
        }

        return false;
    }

    private static void highlightParents() {
        caretPos = codeArea.getCaretPosition();

        if (isLeftDirection())
            caretPrev();
        else
            caretNext();
    }

    private static boolean isInDuration() {
        return lastPosCaret > -1 && lastPosCaret < currText.length();
    }

    private static boolean isLeftDirection() {
        return caretPos + 1 == lastPosCaret || caretPos == lastPosCaret;
    }
}
