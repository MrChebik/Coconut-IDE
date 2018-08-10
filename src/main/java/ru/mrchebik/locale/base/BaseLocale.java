package ru.mrchebik.locale.base;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum BaseLocale {
    PROJECT("Coconut-IDE");

    private String text;

    @Override
    public String toString() {
        return text;
    }
}
