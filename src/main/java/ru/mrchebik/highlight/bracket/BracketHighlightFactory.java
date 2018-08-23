package ru.mrchebik.highlight.bracket;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public abstract class BracketHighlightFactory {
    protected static List<Stack<String>> stack;

    public void clearVariables() {
        stack = new ArrayList<>();
        stack.add(new Stack<>());
        stack.add(new Stack<>());
    }
}
