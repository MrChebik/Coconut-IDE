package ru.mrchebik.helper;

import javafx.scene.control.TextField;
import lombok.SneakyThrows;
import ru.mrchebik.model.ActionPlaces;
import ru.mrchebik.project.Project;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ru.mrchebik.project.Project.pathSource;

public class FileHelper {
    public static void createFilePresenter(ActionPlaces places, TextField name) {
        Path pathFromPlace = places.closeAndGetCreateFilePlace();
        String nameOfFile = name.getText();
        Path path = pathFromPlace.resolve(nameOfFile);
        FileHelper.createFile(path);
    }

    @SneakyThrows(IOException.class)
    public static void createFile(Path path) {
        if (!Files.exists(path)) {
            Files.createFile(path);
        }
    }

    @SneakyThrows(IOException.class)
    public static void createFolder(Path path) {
        if (!Files.exists(path)) {
            Files.createDirectory(path);
        }
    }

    @SneakyThrows(IOException.class)
    public static void deleteFile(Path path) {
        if (Files.exists(path) && path.startsWith(Project.path)) {
            Files.walk(path)
                    .collect(Collectors.toCollection(LinkedList::new))
                    .descendingIterator()
                    .forEachRemaining(p -> {
                        try {
                            Files.delete(p);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        }
    }

    public static String getPathWithoutExtension(Path path) {
        String pathString = path.toString();
        int indexOfLastDot = pathString.lastIndexOf('.');

        return pathString.substring(0, indexOfLastDot);
    }

    public static String getPackageOfRunnable(Path path) {
        Path relative = pathSource.relativize(path);
        String relativePathWithoutExtension = getPathWithoutExtension(relative);

        if (relativePathWithoutExtension.contains(File.separator)) {
            return relativePathWithoutExtension.replaceAll(File.separator, ".");
        } else {
            return relativePathWithoutExtension;
        }
    }

    @SneakyThrows(IOException.class)
    public static String getStructure(String... advanceSuffixes) {
        String[] suffixes = mergeSuffixes(advanceSuffixes);

        return Files.walk(pathSource)
                .filter(p -> {
                    for (String suffix : suffixes) {
                        if (p.toString().endsWith(suffix)) {
                            return true;
                        }
                    }

                    return false;
                })
                .map(Path::toString)
                .collect(Collectors.joining(" "));
    }

    @SneakyThrows(IOException.class)
    public static void writeClassMain(Path path) {
        String classMain = String.join("\n", new String[]{
                "public class Main {",
                "    public static void main(String[] args) {",
                "        ",
                "    }",
                "}"
        });
        Files.write(path, classMain.getBytes());
    }

    public static String[] mergeSuffixes(String... advanceSuffixes) {
        return Stream.concat(Stream.of(".java"), Stream.of(advanceSuffixes))
                .toArray(String[]::new);
    }
}
