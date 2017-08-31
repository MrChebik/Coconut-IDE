package ru.mrchebik.model;

/**
 * Created by mrchebik on 8/29/17.
 */
public class Project {
    private static String name;
    private static String path;

    private static String pathSource;
    private static String pathOut;

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

    public static String getPathSource() {
        return pathSource;
    }

    public static void setPathSource(String pathSource) {
        Project.pathSource = pathSource;
    }

    public static String getPathOut() {
        return pathOut;
    }

    public static void setPathOut(String pathOut) {
        Project.pathOut = pathOut;
    }
}
