package ru.mrchebik.utils.fileFilter;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;

/**
 * Created by mrchebik on 8/31/17.
 */
public class CustomFileFilter implements FilenameFilter {
    private String[] suffixes;

    public CustomFileFilter(String[] suffixes) {
        this.suffixes = suffixes;
    }

    @Override
    public boolean accept(File dir, String name) {
        return Arrays.stream(suffixes)
                .anyMatch(name::endsWith);
    }
}
