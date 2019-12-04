package ru.mrchebik.highlight.pair;

import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.Optional;

@AllArgsConstructor
public enum PairSymbolsType {
    FIGURE('{', '}'),
    ROUND('(', ')'),
    SQUARE('[', ']');

    public char left;
    public char right;

    public static PairSymbols find(char symbol) {
        PairSymbolsType type = Arrays.stream(PairSymbolsType.values())
                .filter(item -> item.right == symbol ||
                        item.left == symbol)
                .findFirst().orElse(null);

        return type != null ?
                new PairSymbols(type.left, type.right) : null;
    }
}
