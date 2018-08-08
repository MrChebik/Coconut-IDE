package ru.mrchebik.project;

import lombok.AllArgsConstructor;

import java.nio.file.Path;

@AllArgsConstructor
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
        Path corePath = Projects.create().getCorePath();

        return name + " - [" +
                (path.startsWith(corePath) ?
                        "~/" + corePath.getFileName().toString() + "/" + corePath.relativize(path) : path)
                + "] - Coconut-IDE 0.2.4";
    }
}
