package ru.mrchebik.highlight.pair;

import javafx.application.Platform;
import org.fxmisc.richtext.CodeArea;
import ru.mrchebik.language.Language;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ParentsHighlight {
    private CodeArea codeArea;

    private int caretPos;
    private String currText;
    private int lastPosCaret = -1;
    private List<PairSymbols> parents;
    private String prevClass;
    private String prevClassSaved;

    private boolean calcHighlight(PairSymbols parent, int caretPos) {
        if (currText.charAt(caretPos) == parent.left) {
            calcNext(parent, caretPos, "figure");

            return true;
        } else if (currText.charAt(caretPos) == parent.right) {
            calcPrev(parent, caretPos, "figure");

            return true;
        }

        return false;
    }

    private boolean calcLeft() {
        for (PairSymbols parent : parents) {
            if (caretPos > 1) {
                if (calcHighlight(parent, caretPos - 1)) {
                    caretPos--;
                    return true;
                }
            }
        }

        return false;
    }

    private void calcNext(PairSymbols parentSymbol, int pos, String classCss) {
        String text = codeArea.getText();
        Stack<Character> stack = new Stack<>();
        for (int i = pos; i < text.length(); i++) {
            if (computeFragment(text, stack, i, parentSymbol.left, parentSymbol.right, pos, classCss)) {
                return;
            }
        }
    }

    private void calcPrev(PairSymbols parentSymbol, int pos, String classCss) {
        String text = codeArea.getText();
        Stack<Character> stack = new Stack<>();
        for (int i = pos; i > 0; i--) {
            if (computeFragment(text, stack, i, parentSymbol.right, parentSymbol.left, pos, classCss)) {
                return;
            }
        }
    }

    private boolean calcRight() {
        for (PairSymbols parent : parents) {
            if (caretPos < currText.length()) {
                if (calcHighlight(parent, caretPos)) {
                    return true;
                }
            }
        }

        return false;
    }

    private void caretNext() {
        if (calcRight()) {
            return;
        }
        calcLeft();
    }

    private void caretPrev() {
        if (calcLeft()) {
            return;
        }
        calcRight();
    }

    private void clearPrevHighlight() {
        if (isInDuration()) {
            for (PairSymbols parent : parents) {
                char symbol = currText.charAt(lastPosCaret);
                if (symbol == parent.left) {
                    calcNext(parent, lastPosCaret, prevClassSaved);
                }
                if (symbol == parent.right) {
                    calcPrev(parent, lastPosCaret, prevClassSaved);
                }
            }
        }
    }

    public void compute(CodeArea codeArea) {
        this.codeArea = codeArea;
        
        currText = codeArea.getText();

        clearPrevHighlight();
        highlightParents();
    }

    private boolean computeFragment(String text, Stack<Character> stack, int i, char fragment, char mirrFrag, int pos, String classCss) {
        if (text.charAt(i) == fragment) {
            stack.push(fragment);
        } else if (text.charAt(i) == mirrFrag) {
            if (stack.peek() == fragment) {
                stack.pop();

                if (stack.size() == 0) {
                    if (codeArea.getStyleOfChar(pos).size() != 0) {
                        String currClass = codeArea.getStyleOfChar(pos).iterator().next();
                        if (pos != lastPosCaret && !currClass.equals(prevClass))
                            prevClassSaved = currClass;
                        prevClass = currClass;
                    } else
                        prevClassSaved = "empty";

                    Platform.runLater(() -> codeArea.setStyleClass(pos, pos + 1, classCss));
                    Platform.runLater(() -> codeArea.setStyleClass(i, i + 1, classCss));

                    return true;
                }
            } else
                return true;
        }

        return false;
    }

    public static ParentsHighlight create() {
        ParentsHighlight parentsHighlight = new ParentsHighlight();
        parentsHighlight.parents = new ArrayList<>();
        parentsHighlight.parents.addAll(Language.pairs);

        return parentsHighlight;
    }

    private void highlightParents() {
        caretPos = codeArea.getCaretPosition();

        if (isLeftDirection()) {
            caretPrev();
        } else {
            caretNext();
        }

        lastPosCaret = caretPos;
    }

    private boolean isInDuration() {
        return lastPosCaret > -1 && lastPosCaret < currText.length();
    }

    private boolean isLeftDirection() {
        return caretPos + 1 == lastPosCaret || caretPos == lastPosCaret;
    }
}
