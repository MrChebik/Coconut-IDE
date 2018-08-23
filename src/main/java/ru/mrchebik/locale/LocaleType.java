package ru.mrchebik.locale;

import lombok.AllArgsConstructor;

import java.util.Arrays;

@AllArgsConstructor
public enum LocaleType {
    English("en"),
    Russian("ru"),
    Ukrainian("ua");

    private String locale;

    public static LocaleType find(String locale) {
        return Arrays.stream(LocaleType.values())
                .filter(item -> item.locale.equals(locale))
                .findFirst()
                .get();
    }

    @Override
    public String toString() {
        return locale;
    }
}
