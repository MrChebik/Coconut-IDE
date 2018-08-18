package ru.mrchebik.project;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum VersionType {
    IDE("Coconut-IDE 0.3.0", " - alpha - cleanup");

    private String version;
    private String additional;

    public String toStringFull() {
        return version + additional;
    }

    @Override
    public String toString() {
        return version;
    }
}
