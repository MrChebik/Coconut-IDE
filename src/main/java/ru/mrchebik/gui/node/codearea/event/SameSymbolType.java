package ru.mrchebik.gui.node.codearea.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum SameSymbolType {
    FIGURE('{', '}'),
    ROUND ('(', ')'),
    SQUARE('[', ']');

    @Getter
    private char left;
    @Getter
    private char right;
}
