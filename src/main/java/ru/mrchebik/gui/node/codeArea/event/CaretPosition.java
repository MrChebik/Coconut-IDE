package ru.mrchebik.gui.node.codeArea.event;

import javafx.application.Platform;
import org.fxmisc.richtext.CodeArea;
import ru.mrchebik.model.ParentSymbol;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class CaretPosition {
    private CodeArea codeArea;

    private int caretPos;
    private String currText;
    private int lastPosCaret = -1;
    private List<ParentSymbol> parents;
    private String prevClass;

    private boolean calcHighlight(ParentSymbol parent, int caretPos) {
        if (currText.charAt(caretPos) == parent.getLeft()) {
            calcNext(parent, caretPos, "figure");

            return true;
        } else if (currText.charAt(caretPos) == parent.getRight()) {
            calcPrev(parent, caretPos, "figure");

            return true;
        }

        return false;
    }

    private boolean calcLeft() {
        for (ParentSymbol parent : parents) {
            if (caretPos > 1) {
                if (calcHighlight(parent, caretPos - 1)) {
                    caretPos--;
                    return true;
                }
            }
        }

        return false;
    }

    private void calcNext(ParentSymbol parentSymbol, int pos, String classCss) {
        String text = codeArea.getText();
        Stack<Character> stack = new Stack<>();
        for (int i = pos; i < text.length(); i++) {
            if (computeFragment(text, stack, i, parentSymbol.getLeft(), parentSymbol.getRight(), pos, classCss)) {
                return;
            }
        }
    }

    private void calcPrev(ParentSymbol parentSymbol, int pos, String classCss) {
        String text = codeArea.getText();
        Stack<Character> stack = new Stack<>();
        for (int i = pos; i > 0; i--) {
            if (computeFragment(text, stack, i, parentSymbol.getRight(), parentSymbol.getLeft(), pos, classCss)) {
                return;
            }
        }
    }

    private boolean calcRight() {
        for (ParentSymbol parent : parents) {
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
            for (ParentSymbol parent : parents) {
                char symbol = currText.charAt(lastPosCaret);
                if (symbol == parent.getLeft()) {
                    calcNext(parent, lastPosCaret, prevClass);
                }
                if (symbol == parent.getRight()) {
                    calcPrev(parent, lastPosCaret, prevClass);
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
                        if (pos != lastPosCaret) {
                            prevClass = codeArea.getStyleOfChar(pos).iterator().next();
                        }
                    } else {
                        prevClass = "empty";
                    }

                    Platform.runLater(() -> codeArea.setStyleClass(pos, pos + 1, classCss));
                    Platform.runLater(() -> codeArea.setStyleClass(i, i + 1, classCss));

                    return true;
                }
            } else {
                return true;
            }
        }

        return false;
    }

    private void computeParents() {
        parents = new ArrayList<>();
        parents.add(new ParentSymbol('{', '}'));
        parents.add(new ParentSymbol('(', ')'));
    }

    public static CaretPosition create() {
        CaretPosition caretPosition = new CaretPosition();
        caretPosition.computeParents();

        return caretPosition;
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
