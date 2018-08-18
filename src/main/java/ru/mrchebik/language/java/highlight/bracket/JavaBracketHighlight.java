package ru.mrchebik.language.java.highlight.bracket;

import com.github.javaparser.JavaToken;
import ru.mrchebik.highlight.bracket.BracketHighlightFactory;
import ru.mrchebik.highlight.pair.PairSymbols;
import ru.mrchebik.language.java.highlight.caret.JavaCaretHighlight;

public class JavaBracketHighlight extends BracketHighlightFactory {
    public void wrapSearchBracket(JavaToken token, PairSymbols pair) {
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

    private void searchBracket(JavaToken token, String fragment, String mirrFrag, int index) {
        if (fragment.equals(token.getText()))
            stack.get(index).push(fragment);
        else if (mirrFrag.equals(token.getText()))
            if (fragment.equals(stack.get(index).peek())) {
                stack.get(index).pop();

                if (stack.get(index).size() == 0) {
                    JavaCaretHighlight.doHighlight(token, "figure", true);
                    return;
                }
            }

        searchNext(token, fragment, mirrFrag, index);
    }

    private void searchNext(JavaToken token, String fragment, String mirrFrag, int index) {
        var token1 = index == 0 ?
                token.getPreviousToken().orElse(null)
                :
                token.getNextToken().orElse(null);
        if (token1 != null)
            searchBracket(token1, fragment, mirrFrag, index);
    }
}
