package ru.mrchebik.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class ParentSymbol {
    @Getter
    private char left;
    @Getter
    private char right;
}
