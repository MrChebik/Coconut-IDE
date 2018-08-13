package ru.mrchebik.highlight.pair;

public class PairSymbols {
    public char left;
    public char right;

    public PairSymbols(PairSymbolsType type) {
        left = type.getLeft();
        right = type.getRight();
    }
}
