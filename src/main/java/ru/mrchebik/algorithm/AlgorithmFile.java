package ru.mrchebik.algorithm;

import lombok.SneakyThrows;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ru.mrchebik.project.Project.pathSource;

public class AlgorithmFile {
    @SneakyThrows(IOException.class)
    public static void createFile(Path path) {
        if (!Files.exists(path))
            Files.createFile(path);
    }

    @SneakyThrows(IOException.class)
    public static void createFolder(Path path) {
        if (!Files.exists(path))
            Files.createDirectory(path);
    }

    @SneakyThrows(IOException.class)
    public static void deleteFile(Path path) {
        if (Files.exists(path))
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

    private static String getPathWithoutExtension(Path path) {
        var pathString = path.toString();
        var indexOfLastDot = pathString.lastIndexOf('.');

        return pathString.substring(0, indexOfLastDot);
    }

    public static String getPackageOfRunnable(Path path) {
        Path relative = pathSource.relativize(path);
        String relativePathWithoutExtension = getPathWithoutExtension(relative);

        return relativePathWithoutExtension.contains(File.separator) ?
                relativePathWithoutExtension.replaceAll(File.separator, ".")
                :
                relativePathWithoutExtension;
    }

    @SneakyThrows(IOException.class)
    public static String getStructure(String... advanceSuffixes) {
        String[] suffixes = mergeSuffixes(advanceSuffixes);

        return Files.walk(pathSource)
                .filter(p -> {
                    for (String suffix : suffixes)
                        if (p.toString().endsWith(suffix))
                            return true;

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
