package ru.mrchebik.highlight.pair;

import lombok.AllArgsConstructor;

import java.util.Arrays;

@AllArgsConstructor
public enum PairSymbolsType {
    FIGURE('{', '}'),
    ROUND ('(', ')'),
    SQUARE('[', ']');

    public char left;
    public char right;

    public static PairSymbols find(char symbol) {
        PairSymbolsType type = Arrays.stream(PairSymbolsType.values())
                .filter(item -> item.right == symbol ||
                        item.left == symbol)
                .findFirst().get();

        return new PairSymbols(type.left, type.right);
    }
}
