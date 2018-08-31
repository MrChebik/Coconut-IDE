package ru.mrchebik.locale;

import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.stream.Collectors;

@AllArgsConstructor
public enum LocaleType {
    English("en"),
    Russian("ru"),
    Ukrainian("ua");

    private String locale;

    public static LocaleType find(String locale) {
        return Arrays.stream(LocaleType.values())
                .filter(item -> item.locale.equals(locale))
                .findFirst().orElse(LocaleType.English);
    }

    @Override
    public String toString() {
        return locale;
    }

    public static String getAll() {
        return Arrays.stream(LocaleType.values())
                .map(LocaleType::toString)
                .collect(Collectors.joining(", "));
    }
}
