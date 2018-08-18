package ru.mrchebik.highlight.caret;

import org.fxmisc.richtext.CodeArea;

public abstract class CaretHighlightFactory {
    protected static CodeArea codeArea;
    protected static String currText;
    protected static int paragraph;
    protected static int column;

    public void compute(CodeArea area) {
        codeArea = area;
        currText = area.getText();

        doClear();
        setParagraphAndColumn();
        highlightNear();
    }

    private void doClear() {
        clearHighlight();
        clearVariables();
    }

    private void setParagraphAndColumn() {
        paragraph = codeArea.getCurrentParagraph() + 1;
        column = codeArea.getCaretColumn() + 1;
    }

    protected void clearVariables() {
        throw new UnsupportedOperationException();
    }

    protected void clearHighlight() {
        throw new UnsupportedOperationException();
    }

    protected void highlightNear() {
        throw new UnsupportedOperationException();
    }
}