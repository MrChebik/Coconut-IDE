package ru.mrchebik.model.autocomplete;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class TextPackage {
    @Getter
    private String text;
    @Getter
    private String packageText;
}
