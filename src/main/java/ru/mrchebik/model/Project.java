package ru.mrchebik.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import ru.mrchebik.process.ExecutorCommand;
import ru.mrchebik.process.io.ErrorProcess;
import ru.mrchebik.settings.PropertyCollector;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
public class Project {
    @Getter @Setter
    private String name;
    @Getter @Setter
    private Path path;
    @Getter @Setter
    private Path pathOut;
    @Getter @Setter
    private Path pathSource;

    private ErrorProcess errorProcess;
    private ExecutorCommand executorCommand;

    public void build() {
        createFolder(path);
        createFolder(pathOut);
        createFolder(pathSource);

        Path pathOfMain = Paths.get(pathSource.toString(), "Main.java");
        createFile(pathOfMain);
        writeClassMain(pathOfMain);
    }

    public Thread compile() {
        Thread thread = new Thread(() -> {
            String pathJDK = PropertyCollector.create().getProperty("jdk");
            if (pathJDK == null) {
                pathJDK = "javac";
            } else {
                pathJDK += File.separator + "bin" + File.separator + "javac";
            }
            String command = pathJDK + " -d " + pathOut.toString() + " " + getStructure();
            executorCommand.execute(command);
        });
        thread.start();

        return thread;
    }

    @SneakyThrows(IOException.class)
    public void createFile(Path path) {
        if (!Files.exists(path)) {
            Files.createFile(path);
        }
    }

    @SneakyThrows(IOException.class)
    public void createFolder(Path path) {
        if (!Files.exists(path)) {
            Files.createDirectory(path);
        }
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

    private String getPathWithoutExtension(Path path) {
        String pathString = path.toString();
        int indexOfLastDot = pathString.lastIndexOf('.');

        return pathString.substring(0, indexOfLastDot);
    }

    private String getPackageOfRunnable(Path path) {
        Path relative = pathSource.relativize(path);
        String relativePathWithoutExtension = getPathWithoutExtension(relative);

        if (relativePathWithoutExtension.contains(File.separator)) {
            return relativePathWithoutExtension.replaceAll(File.separator, ".");
        } else {
            return relativePathWithoutExtension;
        }
    }

    @SneakyThrows(IOException.class)
    private String getStructure(String... advanceSuffixes) {
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

    public String getTitle() {
        Path corePath = Projects.create().getCorePath();

        return name + " - [" +
                (path.startsWith(corePath) ?
                        "~/" + corePath.getFileName().toString() + "/" + corePath.relativize(path) : path)
                + "] - Coconut-IDE 0.1.1";
    }

    private String[] mergeSuffixes(String... advanceSuffixes) {
        return Stream.concat(Stream.of(".java"), Stream.of(advanceSuffixes))
                .toArray(String[]::new);
    }

    public void run(Path path) {
        Thread thread = new Thread(() -> {
            Thread compile = compile();
            try {
                compile.join();
            } catch (InterruptedException ignored) {
            }

            if (!errorProcess.isWasError()) {
                String pathJDK = PropertyCollector.create().getProperty("jdk");
                if (pathJDK == null) {
                    pathJDK = "java";
                } else {
                    pathJDK += File.separator + "bin" + File.separator + "java";
                }
                String command = pathJDK + " -cp " + pathOut.toString() + " " + getPackageOfRunnable(path);
                executorCommand.execute(command);
            }
        });
        thread.start();
    }

    @SneakyThrows(IOException.class)
    private void writeClassMain(Path path) {
        String classMain = String.join("\n", new String[]{
                "public class Main {",
                "    public static void main(String[] args) {",
                "        ",
                "    }",
                "}"
        });
        Files.write(path, classMain.getBytes());
    }
}
