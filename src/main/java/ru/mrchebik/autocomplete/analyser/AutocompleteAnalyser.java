package ru.mrchebik.autocomplete.analyser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class AutocompleteAnalyser extends Thread {
    protected static List<File> files;
    protected static String suffix;

    static {
        files = new ArrayList<>();
    }

    protected static void listFilesForFolder(File entry) {
        for (final File fileEntry : Objects.requireNonNull(entry.listFiles()))
            if (fileEntry.isDirectory())
                listFilesForFolder(fileEntry);
            else if (fileEntry.getName().endsWith("." + suffix))
                files.add(fileEntry);
    }

    public static void callAnalysis(String text, boolean isNew) {
        throw new UnsupportedOperationException();
    }
}
