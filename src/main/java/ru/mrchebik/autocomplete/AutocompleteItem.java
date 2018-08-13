package ru.mrchebik.autocomplete;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class AutocompleteItem {
    @Getter
    private String type;
    @Getter
    private String text;
    @Getter
    private String pasteText;
    @Getter
    private String packageName;
}
