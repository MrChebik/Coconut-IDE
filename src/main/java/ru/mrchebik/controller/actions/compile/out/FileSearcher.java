package ru.mrchebik.controller.actions.compile.out;

import ru.mrchebik.controller.actions.autosave.Autosave;
import ru.mrchebik.controller.actions.autosave.saver.SaveFile;
import ru.mrchebik.model.Project;
import ru.mrchebik.model.controller.actions.autosave.FutureFile;
import ru.mrchebik.utils.fileFilter.CustomFileFilter;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by mrchebik on 9/2/17.
 */
public class FileSearcher {
    String[] suffixes;

    public FileSearcher(String suffix, String... suffixes) {
        String[] computeSuffixes = new String[1 + suffixes.length];
        computeSuffixes[0] = suffix;
        IntStream.range(0, suffixes.length)
                .forEach(i -> computeSuffixes[i + 1] = suffixes[i]);

        this.suffixes = computeSuffixes;
    }

    public void startSeacrh() {
        CustomFileFilter customFileFilter = new CustomFileFilter(suffixes);

        File source = new File(Project.getPathSource());
        File[] filesFromSource = source.listFiles(customFileFilter);
        // TODO see filesFromSource

        if (filesFromSource != null) {
            Path path = Paths.get(Project.getPathOutListStructure());

            String pathsOfFilesInLine = Arrays.stream(filesFromSource)
                    .map(File::getPath)
                    .collect(Collectors.joining("\n"));

            FutureFile futureFile = new FutureFile(path, pathsOfFilesInLine);

            Autosave saver = new SaveFile(futureFile);
            saver.save();
        }
    }
}
