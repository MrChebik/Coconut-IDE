package ru.mrchebik.project;

import ru.mrchebik.algorithm.AlgorithmFile;
import ru.mrchebik.settings.PropertyCollector;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Project {
    public static String name;
    public static Path path;
    public static Path pathOut;
    public static Path pathSource;

    public static boolean isOpen;

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
        AlgorithmFile.createFolder(path);
        AlgorithmFile.createFolder(pathOut);
        AlgorithmFile.createFolder(pathSource);

        Path pathOfMain = Paths.get(pathSource.toString(), "Main.java");
        AlgorithmFile.createFile(pathOfMain);
        AlgorithmFile.writeClassMain(pathOfMain);
    }

    public static String getTitle() {
        var projects = Paths.get(PropertyCollector.projects);

        return name + " - [" +
                (path.startsWith(projects) ?
                        "~/" + projects.getFileName().toString() + "/" + projects.relativize(path) : path)
                + "] - " + VersionType.IDE;
    }
}
