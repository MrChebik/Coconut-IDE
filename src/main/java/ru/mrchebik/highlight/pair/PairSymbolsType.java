package ru.mrchebik.highlight.pair;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum PairSymbolsType {
    FIGURE('{', '}'),
    ROUND ('(', ')'),
    SQUARE('[', ']');

    @Getter
    private char left;
    @Getter
    private char right;
}
