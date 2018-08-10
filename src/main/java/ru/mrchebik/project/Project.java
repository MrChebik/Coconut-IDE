package ru.mrchebik.project;

import java.nio.file.Path;

public class Project {
    public static String name;
    public static Path path;
    public static Path pathOut;
    public static Path pathSource;

    /*public void build() {
        createFolder(path);
        createFolder(pathOut);
        createFolder(pathSource);

        Path pathOfMain = Paths.get(pathSource.toString(), "Main.java");
        createFile(pathOfMain);
        writeClassMain(pathOfMain);
    }*/

    public static String getTitle() {
        var projects = Projects.path;

        return name + " - [" +
                (path.startsWith(projects) ?
                        "~/" + projects.getFileName().toString() + "/" + projects.relativize(path) : path)
                + "] - Coconut-IDE " + VersionType.IDE;
    }
}
