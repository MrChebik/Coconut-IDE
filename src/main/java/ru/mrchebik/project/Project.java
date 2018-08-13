package ru.mrchebik.project;

import ru.mrchebik.helper.FileHelper;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Project {
    public static String name;
    public static Path path;
    public static Path pathOut;
    public static Path pathSource;

    public static void init(String name,
                            Path path,
                            Path pathOut,
                            Path pathSource) {
        Project.name = name;
        Project.path = path;
        Project.pathOut = pathOut;
        Project.pathSource = pathSource;
    }

    public static void build() {
        FileHelper.createFolder(path);
        FileHelper.createFolder(pathOut);
        FileHelper.createFolder(pathSource);

        Path pathOfMain = Paths.get(pathSource.toString(), "Main.java");
        FileHelper.createFile(pathOfMain);
        FileHelper.writeClassMain(pathOfMain);
    }

    public static String getTitle() {
        var projects = Projects.path;

        return name + " - [" +
                (path.startsWith(projects) ?
                        "~/" + projects.getFileName().toString() + "/" + projects.relativize(path) : path)
                + "] - " + VersionType.IDE;
    }
}
