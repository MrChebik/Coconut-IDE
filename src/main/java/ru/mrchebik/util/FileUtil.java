package ru.mrchebik.util;

import lombok.SneakyThrows;
import ru.mrchebik.exception.ExceptionUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ru.mrchebik.project.Project.pathSource;

public class FileUtil extends ExceptionUtils {
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

    public static void createFolders(Path... paths) {
        Arrays.stream(paths)
                .forEach(FileUtil::createFolder);
    }

    @SneakyThrows(IOException.class)
    public static void deleteFile(Path path) {
        if (Files.exists(path))
            Files.walk(path)
                    .collect(Collectors.toCollection(LinkedList::new))
                    .descendingIterator()
                    .forEachRemaining(handlingConsumerWrapper(Files::delete, IOException.class));
    }

    private static String getPathWithoutExtension(Path path) {
        var pathStr = path.toString();
        var lastDot = pathStr.lastIndexOf('.');

        return pathStr.substring(0, lastDot);
    }

    public static String getPackageOfRunnable(Path path) {
        Path relative = pathSource.relativize(path);
        String withoutExt = getPathWithoutExtension(relative);

        return withoutExt.replaceAll(File.separator, ".");
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
        String dflt = String.join("\n", new String[]{
                "public class Main {",
                "    public static void main(String[] args) {",
                "        ",
                "    }",
                "}"
        });
        Files.write(path, dflt.getBytes());
    }

    public static String[] mergeSuffixes(String... advanceSuffixes) {
        return Stream.concat(Stream.of(".java"), Stream.of(advanceSuffixes))
                .toArray(String[]::new);
    }
}
