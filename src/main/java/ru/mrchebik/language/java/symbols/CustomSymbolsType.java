package ru.mrchebik.language.java.symbols;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum CustomSymbolsType {
    TAB("    ");

    @Getter
    private String custom;
}
