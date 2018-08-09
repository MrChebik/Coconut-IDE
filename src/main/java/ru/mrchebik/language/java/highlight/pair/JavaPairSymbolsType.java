package ru.mrchebik.language.java.highlight.pair;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum JavaPairSymbolsType {
    FIGURE('{', '}'),
    ROUND ('(', ')'),
    SQUARE('[', ']');

    @Getter
    private char left;
    @Getter
    private char right;
}
