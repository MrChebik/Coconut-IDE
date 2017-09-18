package ru.mrchebik.model;

import javafx.scene.control.TextArea;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import ru.mrchebik.process.ExecutorCommand;
import ru.mrchebik.process.io.ErrorProcess;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by mrchebik on 8/29/17.
 */
@AllArgsConstructor
public class Project {
    private @Getter @Setter String name;
    private @Getter @Setter Path path;
    private @Getter @Setter Path pathOut;
    private @Getter @Setter Path pathSource;

    private ExecutorCommand executorCommand;
    private ErrorProcess errorProcess;

    public void build() {
        createFolder(path);
        createFolder(pathOut);
        createFolder(pathSource);

        Path pathOfMain = Paths.get(pathSource.toString(), "Main.java");
        createFile(pathOfMain);
        writeClassMain(pathOfMain);
    }

    @SneakyThrows(IOException.class)
    private void writeClassMain(Path path) {
        String classMain = String.join("\n", new String[] {
                "public class Main {",
                "    public static void main(String[] args) {",
                "        ",
                "    }",
                "}"
        });
        Files.write(path, classMain.getBytes());
    }

    public String getTitle() {
        Projects projects = new Projects();
        Path corePath = projects.getPath();

        return name + " - [" + (path.startsWith(corePath) ? "~/" + corePath.getFileName().toString() + "/" + corePath.relativize(path) : path) + "] - Coconut-IDE 0.0.9";
    }

    public Thread compile() {
        Thread thread = new Thread(() -> {
            String command = "javac -d " + pathOut.toString() + " " + getStructure();
            executorCommand.execute(command);
        });
        thread.start();

        return thread;
    }

    public void run(Path path, TextArea outputArea) {
        Thread thread = new Thread(() -> {
            Thread compile = compile();
            try {
                compile.join();
            } catch (InterruptedException ignored) {
            }

            if (!errorProcess.isWasError()) {
                String command = "java -cp " + pathOut.toString() + " " + getPackageOfRunnable(path);
                executorCommand.execute(command);
            }
        });
        thread.start();
    }

    @SneakyThrows(IOException.class)
    public void createFolder(Path path) {
        if (!Files.exists(path))
            Files.createDirectory(path);
    }

    @SneakyThrows(IOException.class)
    public void createFile(Path path) {
        if (!Files.exists(path))
            Files.createFile(path);
    }

    @SneakyThrows(IOException.class)
    public void deleteFile(Path path) {
        if (Files.exists(path) && path.startsWith(this.path)) {
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

    @SneakyThrows(IOException.class)
    private String getStructure(String... advanceSuffixes) {
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

    private String getPackageOfRunnable(Path path) {
        Path relative = pathSource.relativize(path);
        String relativePathWithoutExtension = getPathWithoutExtension(relative);

        return relativePathWithoutExtension.replaceAll(File.separator, ".");
    }

    private String getPathWithoutExtension(Path path) {
        String pathString = path.toString();
        int indexOfLastDot = pathString.lastIndexOf('.');

        return pathString.substring(0, indexOfLastDot);
    }

    private String[] mergeSuffixes(String... advanceSuffixes) {
        String[] computeSuffixes = new String[1 + advanceSuffixes.length];
        computeSuffixes[0] = ".java";
        IntStream.range(0, advanceSuffixes.length)
                .forEach(i -> computeSuffixes[i + 1] = advanceSuffixes[i]);

        return computeSuffixes;
    }
}
