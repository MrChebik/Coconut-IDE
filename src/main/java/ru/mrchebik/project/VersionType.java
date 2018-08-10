package ru.mrchebik.project;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum  VersionType {
    IDE("0.3.0");

    @Getter
    private String version;
}
