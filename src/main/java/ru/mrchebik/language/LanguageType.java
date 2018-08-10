package ru.mrchebik.language;

import lombok.AllArgsConstructor;

import java.util.Arrays;

@AllArgsConstructor
public enum LanguageType {
    Java("java");

    private String language;

    public static LanguageType find(String language) {
        return Arrays.stream(LanguageType.values())
                .filter(item -> item.language.equals(language))
                .findFirst()
                .get();
    }

    @Override
    public String toString() {
        return language;
    }
}
