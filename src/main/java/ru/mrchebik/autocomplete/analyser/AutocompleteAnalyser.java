package ru.mrchebik.autocomplete.analyser;

import org.fxmisc.richtext.CodeArea;

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

    protected void listFilesForFolder(File entry) {
        for (final File fileEntry : Objects.requireNonNull(entry.listFiles()))
            if (fileEntry.isDirectory())
                listFilesForFolder(fileEntry);
            else if (fileEntry.getName().endsWith("." + suffix))
                files.add(fileEntry);
    }

    public void compute(CodeArea area) {
        throw new UnsupportedOperationException();
    }

    public void callAnalysis(String text, boolean isNew) {
        throw new UnsupportedOperationException();
    }
}
