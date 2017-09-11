package ru.mrchebik.model.project;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by mrchebik on 8/29/17.
 */
public class ProjectEntity implements Project {
    private @Getter @Setter String name;

    private @Getter @Setter Path path;

    private @Getter @Setter Path pathOut;

    private @Getter @Setter Path pathSource;

    @Inject
    public ProjectEntity(@Assisted String name,
                         @Assisted Path path,
                         @Assisted Path pathOut,
                         @Assisted Path pathSource) {
        this.name = name;
        this.path = path;
        this.pathOut = pathOut;
        this.pathSource = pathSource;
    }

    @Override
    public void build() {
        createFolder(path);
        createFolder(pathOut);
        createFolder(pathSource);

        String pathOfSourceString = pathSource.toString();
        Path pathOfMain = Paths.get(pathOfSourceString, "Main.java");

        createFile(pathOfMain);
    }

    public void createFolder(Path path) {
        try {
            Files.createDirectory(path);
        } catch (IOException e) {
            // TODO show error window
        }
    }

    public void createFile(Path path) {
        try {
            Files.createFile(path);
        } catch (IOException e) {
            // TODO show error window
        }
    }

    @SneakyThrows(IOException.class)
    public String getStructure(String... advanceSuffixes) {
        String[] suffixes = Stream.of(".java", advanceSuffixes).flatMap(Stream::of)
                .toArray(String[]::new);

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
}
