package ru.mrchebik.model;

import lombok.Getter;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Projects {
    @Getter
    private Path corePath;
    @Getter
    private String corePathString;

    private Projects () {
        corePath = Paths.get(System.getProperty("user.home"), "CoconutProjects");
        corePathString = corePath.toString() + File.separator;
    }

    public static Projects create() {
        return new Projects();
    }
}
