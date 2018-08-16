package ru.mrchebik.highlight.pair;

public class PairSymbols {
    public char left;
    public char right;

    public PairSymbols(PairSymbolsType type) {
        left = type.left;
        right = type.right;
    }

    public PairSymbols(char left, char right) {
        this.left = left;
        this.right = right;
    }
}
