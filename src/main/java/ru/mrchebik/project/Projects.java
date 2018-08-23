package ru.mrchebik.project;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Projects {
    public static Path path;
    public static String pathString;

    static {
        path = Paths.get(System.getProperty("user.home"), "CoconutProjects");
        pathString = path.toString() + File.separator;
    }
}
