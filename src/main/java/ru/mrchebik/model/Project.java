package ru.mrchebik.model;

/**
 * Created by mrchebik on 8/29/17.
 */
public class Project {
    private static String name;
    private static String path;

    public static String getName() {
        return Project.name;
    }

    public static void setName(String name) {
        Project.name = name;
    }

    public static String getPath() {
        return Project.path;
    }

    public static void setPath(String path) {
        Project.path = path;
    }
}
